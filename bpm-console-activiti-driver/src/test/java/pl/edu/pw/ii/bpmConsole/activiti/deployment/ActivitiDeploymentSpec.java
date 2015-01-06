package pl.edu.pw.ii.bpmConsole.activiti.deployment;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.impl.persistence.entity.DeploymentEntity;
import org.junit.Test;
import pl.edu.pw.ii.bpmConsole.interfaces.Deployment;
import pl.edu.pw.ii.bpmConsole.interfaces.exceptions.DeploymentTooBigException;
import pl.edu.pw.ii.bpmConsole.interfaces.exceptions.EmptyDeploymentException;
import pl.edu.pw.ii.bpmConsole.interfaces.exceptions.ProcessDeploymentFailedException;
import pl.edu.pw.ii.bpmConsole.interfaces.exceptions.ZipFileHasNoProcessesException;
import pl.edu.pw.ii.bpmConsole.test.Base64Resource;
import pl.edu.pw.ii.bpmConsole.valueObjects.File;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import static org.mockito.Mockito.*;
import static pl.edu.pw.ii.bpmConsole.test.AssertJThrowableAssert.assertThrown;

public class ActivitiDeploymentSpec {

    private static final String DEPLOYMENT_INVALID_NAME = "deployment.png";
    private static final String DEPLOYMENT_VALID_NAME = "deployment.bpmn";
    private static final String TEST_ARCHIVE_NAME = "testProcesses.bar";
    private static final String[] ARCHIVE_PROCESSES = {"process1.bpmn", "process2.bpmn20.xml", "process3.bpmn"};
    private static final byte[] DEPLOYMENT_CONTENT = new byte[0];
    private static final String NOT_DEPLOYABLE_ARCHIVE_NAME = "notDeployableArchive.zip";

    @Test
    public void shouldNotDeployFileWithInvalidExtension() {
        //given
        File deployment = new File(DEPLOYMENT_CONTENT);
        deployment.fileName = DEPLOYMENT_INVALID_NAME;
        deployment.fileSize = (long) 1;
        RepositoryService repositoryServiceMock = mock(RepositoryService.class);
        ActivitiDeployment activitiDeployment = new ActivitiDeployment(repositoryServiceMock, deployment);
        //when - then
        assertThrown(activitiDeployment::deploy)
                .isInstanceOf(ProcessDeploymentFailedException.class);
        verifyZeroInteractions(repositoryServiceMock);
    }

    @Test
    public void shouldDeploySimpleProcess() {
        //given
        File deployment = new File(DEPLOYMENT_CONTENT);
        deployment.fileName = DEPLOYMENT_VALID_NAME;
        deployment.fileSize = (long) 1;
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
        deployment.fileName = TEST_ARCHIVE_NAME;
        deployment.fileSize = (long) 1;
        RepositoryService repositoryServiceMock = mock(RepositoryService.class, RETURNS_DEEP_STUBS);
        when(
                repositoryServiceMock
                        .createDeployment()
                        .name(any())
                        .addInputStream(any(), any(ByteArrayInputStream.class))
                        .deploy()
        ).thenReturn(new DeploymentEntity());
        ActivitiDeployment activitiDeployment = new ActivitiDeployment(repositoryServiceMock, deployment);
        //when
        activitiDeployment.deploy();
        //then
        verifyDeployment(repositoryServiceMock, ARCHIVE_PROCESSES);
    }

    @Test
    public void shouldThrowExceptionIfDeploymentFailed() throws IOException {
        //given
        File deployment = new File(DEPLOYMENT_CONTENT);
        deployment.fileName = DEPLOYMENT_VALID_NAME;
        deployment.fileSize = (long) 1;
        RepositoryService repositoryServiceMock = mock(RepositoryService.class, RETURNS_DEEP_STUBS);
        when(
                repositoryServiceMock
                        .createDeployment()
                        .name(eq(DEPLOYMENT_VALID_NAME))
                        .addInputStream(eq(DEPLOYMENT_VALID_NAME), any(ByteArrayInputStream.class))
                        .deploy()
        ).thenReturn(null);
        ActivitiDeployment activitiDeployment = new ActivitiDeployment(repositoryServiceMock, deployment);
        //when - then
        assertThrown(activitiDeployment::deploy)
                .isInstanceOf(ProcessDeploymentFailedException.class);
    }

    @Test
    public void shouldThrowExceptionIfZipHasNoDeployableFiles() throws IOException {
        //given
        File deployment = new File();
        deployment.base64 = new Base64Resource(NOT_DEPLOYABLE_ARCHIVE_NAME).toString();
        deployment.fileName = NOT_DEPLOYABLE_ARCHIVE_NAME;
        deployment.fileSize = (long) 1;
        RepositoryService repositoryServiceMock = mock(RepositoryService.class);
        ActivitiDeployment activitiDeployment = new ActivitiDeployment(repositoryServiceMock, deployment);
        //when - then
        assertThrown(activitiDeployment::deploy)
                .isInstanceOf(ZipFileHasNoProcessesException.class);
        verifyZeroInteractions(repositoryServiceMock);
    }

    @Test
    public void shouldThrowExceptionIfTheFileIsTooBig(){
        //given
        File deployment = new File();
        deployment.fileSize = Long.valueOf(Deployment.MAX_FILE_SIZE)+1;
        RepositoryService repositoryServiceMock = mock(RepositoryService.class);
        //when - then
        assertThrown(() -> new ActivitiDeployment(repositoryServiceMock, deployment))
                .isInstanceOf(DeploymentTooBigException.class);
        verifyZeroInteractions(repositoryServiceMock);
    }

    @Test
    public void shouldThrowExceptionIfTheFileIsEmpty() {
        //given
        File deployment = new File();
        deployment.fileSize = (long) 0;
        RepositoryService repositoryServiceMock = mock(RepositoryService.class);
        //when - then
        assertThrown(() -> new ActivitiDeployment(repositoryServiceMock, deployment))
                .isInstanceOf(EmptyDeploymentException.class);
        verifyZeroInteractions(repositoryServiceMock);
    }

    private void verifyDeployment(RepositoryService repositoryServiceMock, String ... names) {
        for (String name : names)
            verify(
                repositoryServiceMock.createDeployment()
            ).name(eq(name));
    }


}