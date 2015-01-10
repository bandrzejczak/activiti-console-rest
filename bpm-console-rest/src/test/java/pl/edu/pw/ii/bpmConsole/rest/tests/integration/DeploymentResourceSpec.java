package pl.edu.pw.ii.bpmConsole.rest.tests.integration;

import org.eclipse.jetty.http.HttpStatus;
import org.junit.Test;
import pl.edu.pw.ii.bpmConsole.test.Base64Resource;
import pl.edu.pw.ii.bpmConsole.valueObjects.DeploymentInfo;
import pl.edu.pw.ii.bpmConsole.valueObjects.File;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static pl.edu.pw.ii.bpmConsole.test.MatchesPredicate.anyMatching;

public class DeploymentResourceSpec extends BpmIntegrationTest {

    public static final String DEPLOYMENT_RESOURCE_PATH = "/deployments";
    public static final String DEPLOYMENT_FILE = "processes/deployment.bpmn";
    public static final String DEPLOYMENT_FILE_PROCESS_NAME = "deploymentTest";
    private static final String DEPLOYMENT_ZIP = "processes/processes.bar";
    private static final String[] DEPLOYMENT_ZIP_PROCESS_NAMES = {"deploymentTest1", "deploymentTest2"};
    public static final String DEPLOYMENT_FILE2 = "processes/deploymentToDelete.bpmn";
    private static final String DEPLOYMENT_FILE2_PROCESS_KEY = "deploymentToDelete";

    @Test
    public void shouldDeployValidProcess() throws IOException {
        //given
        File singleProcess = new Base64Resource(DEPLOYMENT_FILE).toFile();
        //when
        Response response = deployProcess(singleProcess);
        //then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED_201);
        assertThat(listProcesses())
                .has(anyMatching((DeploymentInfo p) -> DEPLOYMENT_FILE_PROCESS_NAME.equals(p.name)));
    }

    @Test
    public void shouldDeployProcessesPackage() throws IOException {
        //given
        File processesArchive = new Base64Resource(DEPLOYMENT_ZIP).toFile();
        //when
        Response response = deployProcess(processesArchive);
        //then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED_201);
        assertThat(listProcesses())
                .has(anyMatching(
                        (DeploymentInfo p) -> DEPLOYMENT_ZIP_PROCESS_NAMES[0].equals(p.name),
                        (DeploymentInfo p) -> DEPLOYMENT_ZIP_PROCESS_NAMES[1].equals(p.name)
                ));
    }

    @Test
    public void shouldRemoveProcessDefinition() throws IOException {
        //given
        File processToDelete = new Base64Resource(DEPLOYMENT_FILE2).toFile();
        deployProcess(processToDelete);
        String processDefinitionId = findProcessDefinitionId(DEPLOYMENT_FILE2_PROCESS_KEY);
        //when
        Response response = removeProcess(processDefinitionId);
        //then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.NO_CONTENT_204);
        assertThat(
                listProcesses()
                        .stream()
                        .noneMatch(p -> p.key.equals(DEPLOYMENT_FILE2_PROCESS_KEY))
        ).isTrue();
    }

    private Response deployProcess(File process) {
        return target(DEPLOYMENT_RESOURCE_PATH)
                .request()
                .post(Entity.entity(process, MediaType.APPLICATION_JSON));
    }

    private List<DeploymentInfo> listProcesses() {
        return target(DEPLOYMENT_RESOURCE_PATH)
                .request()
                .accept(MediaType.APPLICATION_JSON)
                .get(new ListOfDeployments());
    }

    private String findProcessDefinitionId(String processDefinitionKey) {
        return listProcesses()
                .stream()
                .filter(d -> d.key.equals(processDefinitionKey))
                .findAny()
                .get()
                .id;
    }

    private Response removeProcess(String processDefinitionId) {
        return target(DEPLOYMENT_RESOURCE_PATH + "/" + processDefinitionId)
                .request()
                .delete();
    }

    private class ListOfDeployments extends GenericType<List<DeploymentInfo>> {

    }


}
