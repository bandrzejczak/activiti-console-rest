package pl.edu.pw.ii.ActivitiConsole.bundles;

import com.codahale.metrics.health.HealthCheck;
import io.dropwizard.ConfiguredBundle;
import io.dropwizard.servlets.tasks.Task;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import pl.edu.pw.ii.ActivitiConsole.configuration.ActivitiConsoleConfiguration;
import pl.edu.pw.ii.ActivitiConsole.configuration.ActivitiConsoleSpringConfiguration;

import javax.ws.rs.Path;
import javax.ws.rs.ext.Provider;
import java.lang.annotation.Annotation;
import java.util.Map;
import java.util.function.Consumer;

public class SpringBundle implements ConfiguredBundle<ActivitiConsoleConfiguration> {

    AnnotationConfigWebApplicationContext parentSpringContext;
    AnnotationConfigWebApplicationContext springContext;
    Environment environment;

    @Override
    public void initialize(Bootstrap<?> bootstrap) {

    }

    @Override
    public void run(ActivitiConsoleConfiguration configuration, Environment environment) throws Exception {
        this.environment = environment;
        initializeParentContext(configuration);
        initializeContext(configuration);
        addSpringBeansToEnvironment();
        registerSpringContext(environment);
    }

    private void initializeParentContext(ActivitiConsoleConfiguration configuration) {
        parentSpringContext = new AnnotationConfigWebApplicationContext();
        parentSpringContext.refresh();
        parentSpringContext.getBeanFactory().registerSingleton("configuration", configuration);
        parentSpringContext.registerShutdownHook();
        parentSpringContext.start();
    }

    private void initializeContext(ActivitiConsoleConfiguration configuration) {
        springContext = new AnnotationConfigWebApplicationContext();
        springContext.setParent(parentSpringContext);
        springContext.register(ActivitiConsoleSpringConfiguration.class);
        springContext.refresh();
        springContext.registerShutdownHook();
        springContext.start();
        configuration.springContext = springContext;
    }

    private void addSpringBeansToEnvironment() {
        addHealthyChecksToEnvironment();
        addResourcesToEnvironment();
        addProvidersToEnvironment();
        addTasksToEnvironment();
    }

    private void addHealthyChecksToEnvironment(){
        addBeansOfTypeToEnvironment(HealthCheck.class,
                h -> environment.healthChecks().register(h.getKey(), (HealthCheck) h.getValue()));
    }

    private void addResourcesToEnvironment(){
        addAnnotatedBeansToEnvironment(Path.class,
                r -> environment.jersey().register(r.getValue()));
    }

    private void addProvidersToEnvironment(){
        addAnnotatedBeansToEnvironment(Provider.class,
                p -> environment.jersey().register(p.getValue()));
    }

    private void addTasksToEnvironment(){
        addBeansOfTypeToEnvironment(Task.class,
                p -> environment.admin().addTask((Task) p.getValue()));
    }

    private void addBeansOfTypeToEnvironment(Class<?> type, Consumer<Map.Entry<String, ?>> environmentConsumer){
        springContext
                .getBeansOfType(type)
                .entrySet()
                .stream()
                .forEach(environmentConsumer);
    }

    private void addAnnotatedBeansToEnvironment(Class<? extends Annotation> annotation, Consumer<Map.Entry<String, Object>> environmentConsumer){
        springContext
                .getBeansWithAnnotation(annotation)
                .entrySet()
                .stream()
                .forEach(environmentConsumer);
    }

    private void registerSpringContext(Environment environment) {
        environment.servlets().addServletListeners(new ContextLoaderListener(springContext));
    }
}
