package pl.edu.pw.ii.ActivitiConsole;

import org.reflections.Reflections;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.data.Status;
import org.restlet.ext.spring.SpringRouter;
import org.restlet.resource.ServerResource;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;

import java.lang.annotation.Annotation;
import java.util.*;

public class Router extends SpringRouter implements BeanFactoryAware {

    private BeanFactory beanFactory;
    private String resourcesPackage;
    private Map<String, Class<? extends ServerResource>> resources = new HashMap<String, Class<? extends ServerResource>>();
    private List<String> unsecuredResources = new ArrayList<String>();
    private Request request;
    private Response response;

    @Override
    public void handle(Request request, Response response){
        this.request = request;
        this.response = response;
        ServerResource resource = initializeTargetResource();
        if(resource != null)
            resource.handle();
        else
            response.setStatus(Status.CLIENT_ERROR_NOT_FOUND);
    }

    private ServerResource initializeTargetResource() {
        Class<? extends ServerResource> resourceClass = findSuitableClass();
        if(resourceClass == null)
            return null;
        addPathVariablesToRequest(resourceClass);
        ServerResource resource = beanFactory.getBean(resourceClass);
        resource.init(getContext(), request, response);
        return resource;
    }

    private void addPathVariablesToRequest(Class<?> resourceClass) {
        String pathTemplate = getSpringResourcePath(resourceClass);
        List<String> pathParts = request.getResourceRef().getSegments(true);
        pathParts = pathParts.subList(request.getRootRef().getSegments().size(), pathParts.size());
        int i = 0;
        for(String pathTemplateSegment : pathTemplate.substring(1).split("/")){
            if(pathTemplateSegment.matches("\\{(.*?)\\}"))
                request.getAttributes().put(trimBrackets(pathTemplateSegment), pathParts.get(i));
            ++i;
        }
    }

    private String trimBrackets(String pathTemplateSegment) {
        return pathTemplateSegment.substring(1,pathTemplateSegment.length()-1);
    }

    private Class<? extends ServerResource> findSuitableClass() {
        String path = RequestUtils.getRelativePath(request);
        if(resources.containsKey(path))
            return resources.get(path);
        return findSuitableClassWithConditionalMapping(path);
    }

    private Class<? extends ServerResource> findSuitableClassWithConditionalMapping(String path) {
        for(Map.Entry<String, Class<? extends ServerResource>> mapping : resources.entrySet())
            if(path.matches(RequestUtils.createPathRegex(mapping.getKey())))
                return mapping.getValue();
        return null;
    }

    private void scanForResources(){
        Set<Class<?>> annotatedClasses = getClassesAnnotatedWithSpringResource();
        for(Class<?> annotatedClass : annotatedClasses){
            analyzeClass(annotatedClass);
        }
    }

    @SuppressWarnings("unchecked")
    private void analyzeClass(Class<?> annotatedClass) {
        if(!ServerResource.class.isAssignableFrom(annotatedClass))
            return;
        String path = getSpringResourcePath(annotatedClass);
        if(path != null && !resources.containsKey(path)){
            resources.put(path, (Class<? extends ServerResource>) annotatedClass);
            if(!isResourceSecured(annotatedClass))
                unsecuredResources.add(path);
        }
    }

    private boolean isResourceSecured(Class<?> annotatedClass) {
        SpringResource springResource = getSpringResourceAnnotation(annotatedClass);
        return springResource != null && springResource.secured();
    }

    private SpringResource getSpringResourceAnnotation(Class<?> annotatedClass){
        Annotation[] annotations = annotatedClass.getAnnotations();
        for(Annotation annotation : annotations)
            if(annotation instanceof SpringResource)
                return (SpringResource) annotation;
        return null;
    }

    private Set<Class<?>> getClassesAnnotatedWithSpringResource() {
        Reflections reflections = new Reflections(resourcesPackage, new TypeAnnotationsScanner());
        return reflections.getTypesAnnotatedWith(SpringResource.class);
    }

    private String getSpringResourcePath(Class<?> annotatedClass) {
        SpringResource springResource = getSpringResourceAnnotation(annotatedClass);
        return springResource != null ? springResource.value() : null;
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
        scanForResources();
    }

    public boolean isPathSecured(String path){
        return !unsecuredResources.contains(path);
    }

    public void setResourcesPackage(String resourcesPackage) {
        this.resourcesPackage = resourcesPackage;
    }
}