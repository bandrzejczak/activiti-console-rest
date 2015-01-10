package pl.edu.pw.ii.bpmConsole.activiti.deployment;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import pl.edu.pw.ii.bpmConsole.interfaces.exceptions.ProcessDefinitionDoesntExistsException;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static pl.edu.pw.ii.bpmConsole.test.AssertJThrowableAssert.assertThrown;

@RunWith(MockitoJUnitRunner.class)
public class DeploymentRemovingSpec {

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    RepositoryService repositoryServiceMock;

    @Test
    public void shouldThrowExceptionIfProcessDefinitionDoesntExist() {
        //given
        when(
                repositoryServiceMock
                        .createProcessDefinitionQuery()
                        .processDefinitionId(eq("process:1:1"))
                        .singleResult()
        ).thenReturn(null);
        ActivitiDeployments activitiDeployments = new ActivitiDeployments(repositoryServiceMock);
        //when
        assertThrown(() -> activitiDeployments.remove("process:1:1"))
                .isInstanceOf(ProcessDefinitionDoesntExistsException.class);
    }

    @Test
    public void shouldRemoveProcessDefinitionIfExists() {
        //given
        ProcessDefinitionEntity processDefinition = new ProcessDefinitionEntity();
        processDefinition.setDeploymentId("deployment");
        when(
                repositoryServiceMock
                        .createProcessDefinitionQuery()
                        .processDefinitionId(eq("process:1:1"))
                        .singleResult()
        ).thenReturn(processDefinition);
        ActivitiDeployments activitiDeployments = new ActivitiDeployments(repositoryServiceMock);
        //when
        activitiDeployments.remove("process:1:1");
        //then
        verify(
                repositoryServiceMock
        ).deleteDeployment(eq(processDefinition.getDeploymentId()), eq(true));
    }
}
