package pl.edu.pw.ii.BpmConsole.Rest.bundles;

import io.dropwizard.ConfiguredBundle;
import io.dropwizard.auth.AuthFactory;
import io.dropwizard.auth.basic.BasicAuthFactory;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.eclipse.jetty.servlets.CrossOriginFilter;
import pl.edu.pw.ii.BpmConsole.Rest.filters.BpmAuthenticator;
import pl.edu.pw.ii.BpmConsole.Rest.filters.BpmUser;
import pl.edu.pw.ii.BpmConsole.Rest.configuration.BpmConsoleConfiguration;
import pl.edu.pw.ii.BpmConsole.Rest.filters.JSONVulnerabilityProtectionFilter;

import org.glassfish.hk2.utilities.Binder;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;
import java.util.EnumSet;

public class FilterBundle implements ConfiguredBundle<BpmConsoleConfiguration> {

    BpmConsoleConfiguration configuration;
    Environment environment;

    @Override
    public void initialize(Bootstrap<?> bootstrap) {

    }

    @Override
    public void run(BpmConsoleConfiguration configuration, Environment environment) throws Exception {
        this.configuration = configuration;
        this.environment = environment;
        addCrossOriginFilter();
        addActivitiAuthenticatorFilter();
        addJSONVulnerabilityProtectionFilter();
    }

    private void addCrossOriginFilter() {
        FilterRegistration.Dynamic crossOriginFilter = environment
                .servlets()
                .addFilter("CORSFilter", CrossOriginFilter.class);
        configuration.crossOriginConfiguration.applySettingsTo(crossOriginFilter);
        crossOriginFilter.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), true, "/*");
    }

    private void addJSONVulnerabilityProtectionFilter() {
        environment
                .jersey()
                .register(JSONVulnerabilityProtectionFilter.class);
    }

    private void addActivitiAuthenticatorFilter() {
        environment
                .jersey()
                .register(getActivitiAuthenticator());
    }

    private Binder getActivitiAuthenticator() {
        return AuthFactory.binder(new BasicAuthFactory<>(
                configuration.springContext.getBean(BpmAuthenticator.class),
                "ActivitiAuthenticator",
                BpmUser.class
        ));
    }
}
