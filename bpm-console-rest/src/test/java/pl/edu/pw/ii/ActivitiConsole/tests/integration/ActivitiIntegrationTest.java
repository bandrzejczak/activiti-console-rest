package pl.edu.pw.ii.ActivitiConsole.tests.integration;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;
import io.dropwizard.client.JerseyClientBuilder;
import io.dropwizard.testing.junit.DropwizardAppRule;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.test.ActivitiRule;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Rule;
import pl.edu.pw.ii.ActivitiConsole.ActivitiConsoleApplication;
import pl.edu.pw.ii.ActivitiConsole.configuration.ActivitiConsoleConfiguration;
import pl.edu.pw.ii.ActivitiConsole.db.User;

public class ActivitiIntegrationTest {

    public static String CONFIGURATION_FILE = "src/test/resources/activiti-console-test.yml";

    @ClassRule
    public final static DropwizardAppRule<ActivitiConsoleConfiguration> DROPWIZARD_APP_RULE =
            new DropwizardAppRule<>(ActivitiConsoleApplication.class, CONFIGURATION_FILE);

    @Rule
    public ActivitiRule activitiRule = new ActivitiRule(getProcessEngine());

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

    private ProcessEngine getProcessEngine(){
        return DROPWIZARD_APP_RULE.getConfiguration().springContext.getBean(ProcessEngine.class);
    }

}
