package pl.edu.pw.ii.BpmConsole.Rest.tests.integration;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import io.dropwizard.client.JerseyClientBuilder;
import io.dropwizard.testing.junit.DropwizardAppRule;
import org.junit.ClassRule;
import org.junit.Test;
import pl.edu.pw.ii.BpmConsole.Rest.BpmConsoleApplication;
import pl.edu.pw.ii.BpmConsole.Rest.configuration.BpmConsoleConfiguration;
import pl.edu.pw.ii.BpmConsole.Rest.tests.asserts.ClientResponseAssert;

public class CrossOriginFilterTest {

    public static String CONFIGURATION_FILE = "src/test/resources/activiti-console-cross-origin-test.yml";
    public static final String EXAMPLE_ORIGIN = "http://localhost:9000";

    @ClassRule
    public final static DropwizardAppRule<BpmConsoleConfiguration> RULE =
            new DropwizardAppRule<>(BpmConsoleApplication.class, CONFIGURATION_FILE);

    private Client client = new JerseyClientBuilder(RULE.getEnvironment()).build("activiti-client");

    @Test
    public void authorizedEntry() {
        ClientResponse response = resource("/groups").header("Origin", EXAMPLE_ORIGIN).options(ClientResponse.class);
        ClientResponseAssert.assertThat(response).containsHeader("Access-Control-Allow-Origin", EXAMPLE_ORIGIN);
    }

    public WebResource resource(String path){
        String url = String.format("http://localhost:%d%s", RULE.getLocalPort(), path);
        return client.resource(url);
    }

}
