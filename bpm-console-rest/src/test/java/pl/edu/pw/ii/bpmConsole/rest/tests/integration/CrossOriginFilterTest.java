package pl.edu.pw.ii.bpmConsole.rest.tests.integration;

import io.dropwizard.client.JerseyClientBuilder;
import io.dropwizard.testing.junit.DropwizardAppRule;
import org.junit.ClassRule;
import org.junit.Test;
import pl.edu.pw.ii.bpmConsole.rest.BpmConsoleApplication;
import pl.edu.pw.ii.bpmConsole.rest.configuration.BpmConsoleConfiguration;
import pl.edu.pw.ii.bpmConsole.rest.tests.asserts.ResponseAssert;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

public class CrossOriginFilterTest {

    public static String CONFIGURATION_FILE = "src/test/resources/activiti-console-cross-origin-test.yml";
    public static final String EXAMPLE_ORIGIN = "http://localhost:9000";

    @ClassRule
    public final static DropwizardAppRule<BpmConsoleConfiguration> RULE =
            new DropwizardAppRule<>(BpmConsoleApplication.class, CONFIGURATION_FILE);

    private Client client = new JerseyClientBuilder(RULE.getEnvironment()).build("activiti-client");

    @Test
    public void authorizedEntry() {
        Response response = target("/groups").request().header("Origin", EXAMPLE_ORIGIN).options();
        ResponseAssert.assertThat(response).containsHeader("Access-Control-Allow-Origin", EXAMPLE_ORIGIN);
    }

    public WebTarget target(String path){
        String url = String.format("http://localhost:%d%s", RULE.getLocalPort(), path);
        return client.target(url);
    }

}
