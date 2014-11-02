package pl.edu.pw.ii.BpmConsole.Rest.bundles;

import io.dropwizard.ConfiguredBundle;
import io.dropwizard.auth.basic.BasicAuthProvider;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.eclipse.jetty.servlets.CrossOriginFilter;
import pl.edu.pw.ii.BpmConsole.Rest.BpmAuthenticator;
import pl.edu.pw.ii.BpmConsole.Rest.BpmUser;
import pl.edu.pw.ii.BpmConsole.Rest.configuration.BpmConsoleConfiguration;
import pl.edu.pw.ii.BpmConsole.Rest.filters.JSONVulnerabilityProtectionFilter;

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
                .servlets()
                .addFilter("JSONVulnerabilityProtectionFilter", JSONVulnerabilityProtectionFilter.class)
                .addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), true, "/*");
    }

    private void addActivitiAuthenticatorFilter() {
        environment
                .jersey()
                .register(getActivitiAuthenticator());
    }

    private BasicAuthProvider<BpmUser> getActivitiAuthenticator() {
        return new BasicAuthProvider<>(
                configuration.springContext.getBean(BpmAuthenticator.class),
                "ActivitiAuthenticator"
        );
    }
}
