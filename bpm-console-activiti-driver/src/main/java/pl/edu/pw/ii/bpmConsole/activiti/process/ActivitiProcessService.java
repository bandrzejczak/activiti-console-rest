package pl.edu.pw.ii.bpmConsole.activiti.process;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.task.IdentityLink;
import org.activiti.engine.task.IdentityLinkType;
import pl.edu.pw.ii.bpmConsole.interfaces.ProcessService;
import pl.edu.pw.ii.bpmConsole.valueObjects.ProcessDefinitionInfo;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class ActivitiProcessService implements ProcessService {
    private final RepositoryService repositoryService;

    public ActivitiProcessService(RepositoryService repositoryService) {
        this.repositoryService = repositoryService;
    }

    @Override
    public Collection<ProcessDefinitionInfo> listStartableProcesses(String userId, List<String> groups) {
        return repositoryService
                .createProcessDefinitionQuery()
                .active()
                .latestVersion()
                .list()
                .stream()
                .filter(p -> isStartableByUser(p.getId(), userId, groups))
                .map(this::mapProcessDefinition)
                .collect(Collectors.toList());
    }

    private Boolean isStartableByUser(String processDefinitionId, String userId, List<String> groups) {
        List<IdentityLink> identityLinks = repositoryService
                .getIdentityLinksForProcessDefinition(processDefinitionId);
        return identityLinks.isEmpty() ||
                identityLinks
                        .stream()
                        .anyMatch(i -> IdentityLinkType.CANDIDATE.equals(i.getType()) &&
                                (userId.equals(i.getUserId()) || groups.contains(i.getGroupId())));
    }

    private ProcessDefinitionInfo mapProcessDefinition(ProcessDefinition processDefinition) {
        ProcessDefinitionInfo processDefinitionInfo = new ProcessDefinitionInfo();
        processDefinitionInfo.id = processDefinition.getId();
        processDefinitionInfo.name = processDefinition.getName();
        processDefinitionInfo.version = processDefinition.getVersion();
        processDefinitionInfo.deployTime = getDeploymentTime(processDefinition.getDeploymentId());
        return processDefinitionInfo;
    }

    private Date getDeploymentTime(String deploymentId) {
        return repositoryService
                .createDeploymentQuery()
                .deploymentId(deploymentId)
                .singleResult()
                .getDeploymentTime();
    }
}
