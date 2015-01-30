package pl.edu.pw.ii.bpmConsole.activiti.process;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.task.IdentityLink;
import org.activiti.engine.task.IdentityLinkType;
import pl.edu.pw.ii.bpmConsole.interfaces.ProcessService;
import pl.edu.pw.ii.bpmConsole.interfaces.exceptions.NoSuchProcessException;
import pl.edu.pw.ii.bpmConsole.interfaces.exceptions.ProcessStartForbiddenException;
import pl.edu.pw.ii.bpmConsole.valueObjects.ProcessDefinitionInfo;
import pl.edu.pw.ii.bpmConsole.valueObjects.ProcessInstanceInfo;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class ActivitiProcessService implements ProcessService {
    private final ProcessEngine processEngine;

    public ActivitiProcessService(ProcessEngine processEngine) {
        this.processEngine = processEngine;
    }

    @Override
    public Collection<ProcessDefinitionInfo> listStartableProcesses(String userId, List<String> groups) {
        return processEngine
                .getRepositoryService()
                .createProcessDefinitionQuery()
                .active()
                .latestVersion()
                .list()
                .stream()
                .filter(p -> isStartableByUser(p.getId(), userId, groups))
                .map(this::mapProcessDefinition)
                .collect(Collectors.toList());
    }

    @Override
    public void startProcess(String processDefinitionId, String userId, List<String> groups) {
        verifyProcessDefinitionExists(processDefinitionId);
        if (!isStartableByUser(processDefinitionId, userId, groups))
            throw new ProcessStartForbiddenException(processDefinitionId);
        processEngine.getIdentityService().setAuthenticatedUserId(userId);
        processEngine.getRuntimeService().startProcessInstanceById(processDefinitionId);
    }

    @Override
    public List<ProcessInstanceInfo> listProcessInstances() {
        return new ProcessInstances(processEngine).list();
    }

    private Boolean isStartableByUser(String processDefinitionId, String userId, List<String> groups) {
        List<IdentityLink> identityLinks = processEngine.getRepositoryService()
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
        return processEngine
                .getRepositoryService()
                .createDeploymentQuery()
                .deploymentId(deploymentId)
                .singleResult()
                .getDeploymentTime();
    }

    private void verifyProcessDefinitionExists(String processDefinitionId) {
        if (processEngine
                .getRepositoryService()
                .createProcessDefinitionQuery()
                .active()
                .processDefinitionId(processDefinitionId)
                .singleResult() == null)
            throw new NoSuchProcessException(processDefinitionId);
    }
}
