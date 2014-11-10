package pl.edu.pw.ii.BpmConsole.Rest.resources;

import javax.ws.rs.HttpMethod;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ResourceActionsResolver {

    private final Class<?> resource;
    private List<ResourceAction> resourceActions = new ArrayList<>();

    public ResourceActionsResolver(Class<?> resource) {
        this.resource = resource;
    }

    public static ResourceActionsResolver forResource(Class<?> resource) {
        return new ResourceActionsResolver(resource);
    }

    public List<ResourceAction> resolve() {
        addResourceActionsForClassMethods();
        return resourceActions;
    }

    private void addResourceActionsForClassMethods() {
        Method[] resourceMethods = resource.getMethods();
        Arrays
            .stream(resourceMethods)
            .filter(this::isActionMethod)
            .forEach(this::addResourceAction);
    }

    private boolean isActionMethod(Method method){
        return Arrays
                .stream(method.getAnnotations())
                .anyMatch(this::hasHttpMethodAnnotation);
    }

    private void addResourceAction(Method method) {
        String httpMethod = getHttpMethod(method);
        resourceActions.add(
                new ResourceAction(
                        method.getName(),
                        httpMethod
                )
        );
    }

    private String getHttpMethod(Method method) {
        return Arrays
                .stream(method.getAnnotations())
                .filter(this::hasHttpMethodAnnotation)
                .map(this::getHttpMethodAnnotationValue)
                .findFirst()
                .get();
    }

    private boolean hasHttpMethodAnnotation(Annotation annotation) {
        return getHttpMethodAnnotation(annotation) != null;
    }

    private String getHttpMethodAnnotationValue(Annotation annotation) {
        return getHttpMethodAnnotation(annotation).value();
    }

    private HttpMethod getHttpMethodAnnotation(Annotation annotation) {
        return annotation.annotationType().getAnnotation(HttpMethod.class);
    }
}
