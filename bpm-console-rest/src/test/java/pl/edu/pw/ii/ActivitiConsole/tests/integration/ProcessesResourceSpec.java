/*package pl.edu.pw.ii.ActivitiConsole.tests.integration;

import com.sun.jersey.api.client.GenericType;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.test.Deployment;
import org.junit.Test;

import java.util.List;

import static org.fest.assertions.api.Assertions.assertThat;

public class ProcessesResourceSpec extends ActivitiIntegrationTest  {

    @Test
    @Deployment(resources = "processes/deployment.bpmn")
    public void shouldListAvailableProcessDefinitions(){
        //given
        ProcessDefinition expectedProcess = getDeployedProcess();
        //when
        List<ActivitiProcessDefinition> processDefinitions =
                resource("/processes").get(new GenericType<>(List.class));
        //then
        assertThat(processDefinitions).hasSize(1);
        ActivitiProcessDefinition deployedProcess = processDefinitions.get(0);
        assertThat(deployedProcess.getDeploymentId()).isEqualTo(expectedProcess.getDeploymentId());
        assertThat(deployedProcess.getKey()).isEqualTo(expectedProcess.getKey());
    }

    private ProcessDefinition getDeployedProcess() {
        return activitiRule.getRepositoryService().createProcessDefinitionQuery().singleResult();
    }


}*/