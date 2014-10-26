package pl.edu.pw.ii.ActivitiConsole;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import pl.edu.pw.ii.ActivitiConsole.bundles.FilterBundle;
import pl.edu.pw.ii.ActivitiConsole.bundles.SpringBundle;
import pl.edu.pw.ii.ActivitiConsole.configuration.ActivitiConsoleConfiguration;

public class ActivitiConsoleApplication extends Application<ActivitiConsoleConfiguration> {


    public static void main(String[] args) throws Exception {
        new ActivitiConsoleApplication().run(args);
    }

    @Override
    public String getName(){
        return "activiti-console";
    }

    @Override
    public void initialize(Bootstrap<ActivitiConsoleConfiguration> bootstrap) {
        //TODO research what does it do?
        bootstrap.getObjectMapper().setSerializationInclusion(JsonInclude.Include.NON_NULL);
        bootstrap.addBundle(new SpringBundle());
        bootstrap.addBundle(new FilterBundle());
    }

    @Override
    public void run(ActivitiConsoleConfiguration configuration, Environment environment) throws Exception {

    }

}
