package pl.edu.pw.ii.bpmConsole.activiti.deployment;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.impl.persistence.entity.DeploymentEntity;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.persistence.entity.SuspensionState;
import org.activiti.engine.repository.ProcessDefinition;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import pl.edu.pw.ii.bpmConsole.valueObjects.DeploymentInfo;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Base64;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DeploymentListingSpec {

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    RepositoryService repositoryServiceMock;

    @Test
    public void shouldReturnEmptyListIfNoDeploymentsExist() {
        //given
        when(repositoryServiceMock.createProcessDefinitionQuery().list())
                .thenReturn(Collections.emptyList());
        ActivitiDeployments activitiDeployments = new ActivitiDeployments(repositoryServiceMock);
        //when
        List<DeploymentInfo> deployments = activitiDeployments.toList();
        //then
        assertThat(deployments).isEmpty();
    }

    @Test
    public void shouldReturnListOfDeployments() {
        //given
        Date now = new Date();
        ProcessDefinition testProcess = prepareTestProcessDefinition();
        DeploymentEntity testDeployment = new DeploymentEntity();
        testDeployment.setDeploymentTime(now);
        byte[] processDiagram = {0, 1, 2};
        InputStream processDiagramStream = new ByteArrayInputStream(processDiagram);
        when(repositoryServiceMock.createProcessDefinitionQuery().list())
                .thenReturn(Collections.singletonList(testProcess));
        when(repositoryServiceMock.createDeploymentQuery().deploymentId(eq(testProcess.getDeploymentId())).singleResult())
                .thenReturn(testDeployment);
        when(repositoryServiceMock.getProcessDiagram(eq(testProcess.getId())))
                .thenReturn(processDiagramStream);
        ActivitiDeployments activitiDeployments = new ActivitiDeployments(repositoryServiceMock);
        //when
        List<DeploymentInfo> deployments = activitiDeployments.toList();
        //then
        assertThat(deployments).hasSize(1);
        DeploymentInfo deployment = deployments.get(0);
        assertThat(deployment.id).isEqualTo(testProcess.getId());
        assertThat(deployment.name).isEqualTo(testProcess.getName());
        assertThat(deployment.version).isEqualTo(testProcess.getVersion());
        assertThat(deployment.description).isEqualTo(testProcess.getDescription());
        assertThat(deployment.active).isNotEqualTo(testProcess.isSuspended());
        assertThat(deployment.deploymentTime).isEqualTo(testDeployment.getDeploymentTime());
        assertThat(deployment.diagramBase64).isEqualTo(Base64.getEncoder().encodeToString(processDiagram));
    }

    private ProcessDefinition prepareTestProcessDefinition() {
        ProcessDefinitionEntity processDefinition = new ProcessDefinitionEntity();
        processDefinition.setId("process:1:1");
        processDefinition.setVersion(1);
        processDefinition.setDeploymentId("2");
        processDefinition.setDescription("Test description");
        processDefinition.setName("Process");
        processDefinition.setSuspensionState(SuspensionState.ACTIVE.getStateCode());
        return processDefinition;
    }

}
