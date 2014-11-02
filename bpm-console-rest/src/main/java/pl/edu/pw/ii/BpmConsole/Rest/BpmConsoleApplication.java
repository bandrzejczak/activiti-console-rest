package pl.edu.pw.ii.BpmConsole.Rest;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import pl.edu.pw.ii.BpmConsole.Rest.bundles.FilterBundle;
import pl.edu.pw.ii.BpmConsole.Rest.bundles.SpringBundle;
import pl.edu.pw.ii.BpmConsole.Rest.configuration.BpmConsoleConfiguration;

public class BpmConsoleApplication extends Application<BpmConsoleConfiguration> {


    public static void main(String[] args) throws Exception {
        new BpmConsoleApplication().run(args);
    }

    @Override
    public String getName(){
        return "activiti-console";
    }

    @Override
    public void initialize(Bootstrap<BpmConsoleConfiguration> bootstrap) {
        bootstrap.addBundle(new SpringBundle());
        bootstrap.addBundle(new FilterBundle());
        setJacksonSerializationInclusion(bootstrap);
    }

    private void setJacksonSerializationInclusion(Bootstrap<BpmConsoleConfiguration> bootstrap) {
        //Jackson will map to JSON only non-null properties
        bootstrap.getObjectMapper().setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    @Override
    public void run(BpmConsoleConfiguration configuration, Environment environment) throws Exception {

    }

}
