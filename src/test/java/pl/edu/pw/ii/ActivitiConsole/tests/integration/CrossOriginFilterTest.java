package pl.edu.pw.ii.ActivitiConsole.tests.integration;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import io.dropwizard.client.JerseyClientBuilder;
import io.dropwizard.testing.junit.DropwizardAppRule;
import org.junit.ClassRule;
import org.junit.Test;
import pl.edu.pw.ii.ActivitiConsole.ActivitiConsoleApplication;
import pl.edu.pw.ii.ActivitiConsole.configuration.ActivitiConsoleConfiguration;

import static pl.edu.pw.ii.ActivitiConsole.tests.asserts.ClientResponseAssert.assertThat;

public class CrossOriginFilterTest {

    public static String CONFIGURATION_FILE = "src/test/resources/activiti-console-cross-origin-test.yml";
    public static final String EXAMPLE_ORIGIN = "http://localhost:9000";

    @ClassRule
    public final static DropwizardAppRule<ActivitiConsoleConfiguration> RULE =
            new DropwizardAppRule<>(ActivitiConsoleApplication.class, CONFIGURATION_FILE);

    private Client client = new JerseyClientBuilder(RULE.getEnvironment()).build("activiti-client");

    @Test
    public void authorizedEntry() {
        ClientResponse response = resource("/groups").header("Origin", EXAMPLE_ORIGIN).options(ClientResponse.class);
        assertThat(response).containsHeader("Access-Control-Allow-Origin", EXAMPLE_ORIGIN);
    }

    public WebResource resource(String path){
        String url = String.format("http://localhost:%d%s", RULE.getLocalPort(), path);
        return client.resource(url);
    }

}
