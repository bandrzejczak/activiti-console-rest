package pl.edu.pw.ii.bpmConsole.activiti.deployment;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.ProcessDefinition;
import org.apache.commons.io.IOUtils;
import pl.edu.pw.ii.bpmConsole.interfaces.exceptions.ProcessDefinitionDoesntExistsException;
import pl.edu.pw.ii.bpmConsole.valueObjects.DeploymentInfo;

import java.io.IOException;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ActivitiDeployments {
    private final RepositoryService repositoryService;

    public ActivitiDeployments(RepositoryService repositoryService) {
        this.repositoryService = repositoryService;
    }

    public List<DeploymentInfo> toList() {
        return findProcessDefinitions()
                .stream()
                .map(this::mapToDeploymentInfo)
                .collect(Collectors.toList());
    }

    private List<ProcessDefinition> findProcessDefinitions() {
        return repositoryService
                .createProcessDefinitionQuery()
                .list();
    }

    private DeploymentInfo mapToDeploymentInfo(ProcessDefinition processDefinition) {
        DeploymentInfo deploymentInfo = new DeploymentInfo();
        deploymentInfo.id = processDefinition.getId();
        deploymentInfo.version = processDefinition.getVersion();
        deploymentInfo.name = processDefinition.getName();
        deploymentInfo.key = processDefinition.getKey();
        deploymentInfo.description = processDefinition.getDescription();
        deploymentInfo.active = !processDefinition.isSuspended();
        deploymentInfo.deploymentTime = findDeploymentTime(processDefinition.getDeploymentId());
        deploymentInfo.diagramBase64 = prepareProcessDiagram(processDefinition.getId());
        return deploymentInfo;
    }

    private Date findDeploymentTime(String deploymentId) {
        return repositoryService
                .createDeploymentQuery()
                .deploymentId(deploymentId)
                .singleResult()
                .getDeploymentTime();
    }

    private String prepareProcessDiagram(String processDefinitionId) {
        try {
            return Base64
                    .getEncoder()
                    .encodeToString(
                            IOUtils.toByteArray(
                                    repositoryService.getProcessDiagram(processDefinitionId)
                            )
                    );
        } catch (IOException e) {
            return "";
        }
    }

    public void remove(String processDefinitionId) {
        ProcessDefinition processDefinition = findProcessDefinition(processDefinitionId)
                .map(p -> p)
                .orElseThrow(() -> new ProcessDefinitionDoesntExistsException(processDefinitionId));
        repositoryService.deleteDeployment(processDefinition.getDeploymentId(), true);
    }

    private Optional<ProcessDefinition> findProcessDefinition(String processDefinitionId) {
        return Optional.ofNullable(repositoryService
                .createProcessDefinitionQuery()
                .processDefinitionId(processDefinitionId)
                .singleResult());
    }
}
