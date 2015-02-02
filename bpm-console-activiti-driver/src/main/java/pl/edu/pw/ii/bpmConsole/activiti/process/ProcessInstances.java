package pl.edu.pw.ii.bpmConsole.activiti.process;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.task.IdentityLink;
import org.activiti.engine.task.IdentityLinkType;
import org.activiti.engine.task.Task;
import pl.edu.pw.ii.bpmConsole.activiti.task.ActivitiTasks;
import pl.edu.pw.ii.bpmConsole.interfaces.exceptions.NoSuchProcessException;
import pl.edu.pw.ii.bpmConsole.interfaces.exceptions.ProcessStartForbiddenException;
import pl.edu.pw.ii.bpmConsole.valueObjects.AuthUser;
import pl.edu.pw.ii.bpmConsole.valueObjects.ProcessDefinitionInfo;
import pl.edu.pw.ii.bpmConsole.valueObjects.ProcessInstanceInfo;
import pl.edu.pw.ii.bpmConsole.valueObjects.TaskInfo;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

class ProcessInstances {
    private final ProcessEngine processEngine;

    public ProcessInstances(ProcessEngine processEngine) {
        this.processEngine = processEngine;
    }

    public List<ProcessInstanceInfo> list() {
        return processEngine
                .getHistoryService()
                .createHistoricProcessInstanceQuery()
                .unfinished()
                .list()
                .stream()
                .map(this::mapProcessInstance)
                .collect(Collectors.toList());
    }

    private ProcessInstanceInfo mapProcessInstance(HistoricProcessInstance processInstance) {
        ProcessInstanceInfo processInstanceInfo = new ProcessInstanceInfo();
        processInstanceInfo.id = processInstance.getId();
        processInstanceInfo.processDefinitionId = processInstance.getProcessDefinitionId();
        processInstanceInfo.name = getProcessDefinitionName(processInstance.getProcessDefinitionId());
        processInstanceInfo.startTime = processInstance.getStartTime();
        Optional<Task> currentTaskOptional = findCurrentTask(processInstance.getId());
        if (currentTaskOptional.isPresent()) {
            Task currentTask = currentTaskOptional.get();
            processInstanceInfo.assignee = currentTask.getAssignee();
            processInstanceInfo.currentTask = new ActivitiTasks(processEngine).mapTask(currentTask);
        } else
            processInstanceInfo.currentTask = new TaskInfo();
        return processInstanceInfo;
    }

    private String getProcessDefinitionName(String processDefinitionId) {
        return processEngine
                .getRepositoryService()
                .createProcessDefinitionQuery()
                .processDefinitionId(processDefinitionId)
                .singleResult()
                .getName();
    }

    private Optional<Task> findCurrentTask(String id) {
        return processEngine
                .getTaskService()
                .createTaskQuery()
                .processInstanceId(id)
                .list()
                .stream()
                .findAny();
    }

    public Collection<ProcessDefinitionInfo> listStartable(AuthUser user) {
        return processEngine
                .getRepositoryService()
                .createProcessDefinitionQuery()
                .active()
                .latestVersion()
                .list()
                .stream()
                .filter(p -> isStartableByUser(p.getId(), user))
                .map(this::mapProcessDefinition)
                .collect(Collectors.toList());
    }

    public void start(String processDefinitionId, AuthUser user) {
        verifyProcessDefinitionExists(processDefinitionId);
        if (!isStartableByUser(processDefinitionId, user))
            throw new ProcessStartForbiddenException(processDefinitionId);
        startProcess(processDefinitionId, user);
    }

    private synchronized void startProcess(String processDefinitionId, AuthUser user) {
        try{
            processEngine.getIdentityService().setAuthenticatedUserId(user.id);
            processEngine.getRuntimeService().startProcessInstanceById(processDefinitionId);
        } finally {
            processEngine.getIdentityService().setAuthenticatedUserId(null);
        }
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

    private Boolean isStartableByUser(String processDefinitionId, AuthUser user) {
        List<IdentityLink> identityLinks = processEngine.getRepositoryService()
                .getIdentityLinksForProcessDefinition(processDefinitionId);
        return identityLinks.isEmpty() ||
                identityLinks
                        .stream()
                        .anyMatch(i -> IdentityLinkType.CANDIDATE.equals(i.getType()) &&
                                (user.id.equals(i.getUserId()) || user.groups.contains(i.getGroupId())));
    }
}
