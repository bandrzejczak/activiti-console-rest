package pl.edu.pw.ii.bpmConsole.activiti.process;

import org.activiti.engine.RepositoryService;
import org.junit.Test;
import pl.edu.pw.ii.bpmConsole.test.Base64Resource;
import pl.edu.pw.ii.bpmConsole.valueObjects.File;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import static org.mockito.Mockito.*;

public class ActivitiDeploymentSpec {

    private static final String DEPLOYMENT_INVALID_NAME = "deployment.png";
    private static final String DEPLOYMENT_VALID_NAME = "deployment.bpmn";
    private static final String TEST_ARCHIVE_NAME = "testProcesses.bar";
    private static final String[] ARCHIVE_PROCESSES = {"process1.bpmn", "process2.bpmn20.xml", "process3.bpmn"};
    private static final byte[] DEPLOYMENT_CONTENT = new byte[0];

    @Test
    public void shouldNotDeployFileWithInvalidExtension() {
        //given
        File deployment = new File(DEPLOYMENT_CONTENT);
        deployment.filename = DEPLOYMENT_INVALID_NAME;
        RepositoryService repositoryServiceMock = mock(RepositoryService.class, RETURNS_DEEP_STUBS);
        ActivitiDeployment activitiDeployment = new ActivitiDeployment(repositoryServiceMock, deployment);
        //when
        activitiDeployment.deploy();
        //then
        verify(
                repositoryServiceMock
                        .createDeployment()
                        .name(DEPLOYMENT_INVALID_NAME)
                        .addInputStream(eq(DEPLOYMENT_INVALID_NAME), any(ByteArrayInputStream.class)),
                never()
        ).deploy();
    }

    @Test
    public void shouldDeploySimpleProcess() {
        //given
        File deployment = new File(DEPLOYMENT_CONTENT);
        deployment.filename = DEPLOYMENT_VALID_NAME;
        RepositoryService repositoryServiceMock = mock(RepositoryService.class, RETURNS_DEEP_STUBS);
        ActivitiDeployment activitiDeployment = new ActivitiDeployment(repositoryServiceMock, deployment);
        //when
        activitiDeployment.deploy();
        //then
        verifyDeployment(repositoryServiceMock, DEPLOYMENT_VALID_NAME);
    }

    @Test
    public void shouldDeployAllValidFilesInsideZip() throws IOException {
        //given
        File deployment = new File();
        deployment.base64 = new Base64Resource(TEST_ARCHIVE_NAME).toString();
        deployment.filename = TEST_ARCHIVE_NAME;
        RepositoryService repositoryServiceMock = mock(RepositoryService.class, RETURNS_DEEP_STUBS);
        ActivitiDeployment activitiDeployment = new ActivitiDeployment(repositoryServiceMock, deployment);
        //when
        activitiDeployment.deploy();
        //then
        verifyDeployment(repositoryServiceMock, ARCHIVE_PROCESSES);
    }

    private void verifyDeployment(RepositoryService repositoryServiceMock, String... names) {
        for (String name : names) {
            verify(
                    repositoryServiceMock
                            .createDeployment()
                            .name(eq(name))
                            .addInputStream(eq(name), any(ByteArrayInputStream.class))
            ).deploy();
        }
    }


}