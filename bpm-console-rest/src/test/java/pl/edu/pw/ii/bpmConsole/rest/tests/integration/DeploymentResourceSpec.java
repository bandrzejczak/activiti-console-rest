package pl.edu.pw.ii.bpmConsole.rest.tests.integration;

import org.eclipse.jetty.http.HttpStatus;
import org.junit.Test;
import pl.edu.pw.ii.bpmConsole.test.Base64Resource;
import pl.edu.pw.ii.bpmConsole.valueObjects.File;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;

public class DeploymentResourceSpec extends BpmIntegrationTest {

    public static final String DEPLOYMENT_RESOURCE_PATH = "/deployment";
    public static final String DEPLOYMENT_FILE = "deployment.bpmn";
    public static final String DEPLOYMENT_PATH = "processes/" + DEPLOYMENT_FILE;
    private static final String DEPLOYMENT_ZIP_FILE = "processes.bar";
    private static final String DEPLOYMENT_ZIP_PATH = "processes/" + DEPLOYMENT_ZIP_FILE;
    private static final Collection<String> DEPLOYMENT_ZIP_CONTENT = Arrays.asList("process1.bpmn", "process2.bpmn20.xml");

    @Test
    public void shouldDeployValidProcess() throws IOException {
        //given
        File singleProcess = new File();
        singleProcess.base64 = new Base64Resource(DEPLOYMENT_PATH).toString();
        singleProcess.fileName = DEPLOYMENT_FILE;
        singleProcess.fileSize = (long) 1;
        //when
        Response response = target(DEPLOYMENT_RESOURCE_PATH)
                .request()
                .post(Entity.entity(singleProcess, MediaType.APPLICATION_JSON));
        //then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED_201);
        assertThat(getProcessEngine().listDeployments()).contains(DEPLOYMENT_FILE);
    }

    @Test
    public void shouldDeployProcessesPackage() throws IOException {
        //given
        File singleProcess = new File();
        singleProcess.base64 = new Base64Resource(DEPLOYMENT_ZIP_PATH).toString();
        singleProcess.fileName = DEPLOYMENT_ZIP_FILE;
        singleProcess.fileSize = (long) 1;
        //when
        Response response = target(DEPLOYMENT_RESOURCE_PATH)
                .request()
                .post(Entity.entity(singleProcess, MediaType.APPLICATION_JSON));
        //then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED_201);
        assertThat(getProcessEngine().listDeployments()).containsAll(DEPLOYMENT_ZIP_CONTENT);
    }
}
