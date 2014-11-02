package pl.edu.pw.ii.BpmConsole.Rest.tests.integration;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;
import io.dropwizard.client.JerseyClientBuilder;
import io.dropwizard.testing.junit.DropwizardAppRule;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import pl.edu.pw.ii.BpmConsole.Rest.BpmConsoleApplication;
import pl.edu.pw.ii.BpmConsole.Rest.configuration.BpmConsoleConfiguration;
import pl.edu.pw.ii.BpmConsole.Rest.db.User;

public class ActivitiIntegrationTest {

    public static String CONFIGURATION_FILE = "src/test/resources/activiti-console-test.yml";

    @ClassRule
    public final static DropwizardAppRule<BpmConsoleConfiguration> DROPWIZARD_APP_RULE =
            new DropwizardAppRule<>(BpmConsoleApplication.class, CONFIGURATION_FILE);

    static Client client;

    @BeforeClass
    public static void initClient(){
        client = new JerseyClientBuilder(DROPWIZARD_APP_RULE.getEnvironment()).build("activiti-client");
        client.addFilter(new HTTPBasicAuthFilter(User.ADMIN.login, User.ADMIN.password));
        client.addFilter(new JsonVulnerabilityPrefixStripperFilter());
    }

    public WebResource resource(String path){
        String url = String.format("http://localhost:%d%s", DROPWIZARD_APP_RULE.getLocalPort(), path);
        return client.resource(url);
    }

}
