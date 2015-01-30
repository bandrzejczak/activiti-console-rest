package pl.edu.pw.ii.bpmConsole.activiti.task;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.task.Task;
import pl.edu.pw.ii.bpmConsole.activiti.user.ActivitiUserRights;
import pl.edu.pw.ii.bpmConsole.interfaces.exceptions.ClaimForbiddenException;
import pl.edu.pw.ii.bpmConsole.interfaces.exceptions.NoSuchTaskException;
import pl.edu.pw.ii.bpmConsole.interfaces.exceptions.UnclaimForbiddenException;
import pl.edu.pw.ii.bpmConsole.valueObjects.Rights;
import pl.edu.pw.ii.bpmConsole.valueObjects.TaskInfo;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ActivitiTasks {

    private final ProcessEngine processEngine;

    public ActivitiTasks(ProcessEngine processEngine) {
        this.processEngine = processEngine;
    }

    public List<TaskInfo> listAssignedTo(String userId) {
        return mapTasks(
                processEngine
                        .getTaskService()
                        .createTaskQuery()
                        .active()
                        .taskAssignee(userId)
                        .list()
        );
    }

    public List<TaskInfo> listAvailableFor(String userId, List<String> userGroups) {
        return mapTasks(
                processEngine
                        .getTaskService()
                        .createTaskQuery()
                        .active()
                        .or()
                        .taskCandidateUser(userId)
                        .taskCandidateGroupIn(userGroups)
                        .endOr()
                        .list()
        );
    }

    private List<TaskInfo> mapTasks(List<Task> tasks) {
        return tasks
                .stream()
                .map(this::mapTask)
                .collect(Collectors.toList());
    }

    public TaskInfo mapTask(Task task) {
        TaskInfo taskInfo = new TaskInfo();
        taskInfo.id = task.getId();
        taskInfo.name = task.getName();
        taskInfo.priority = task.getPriority();
        taskInfo.dueDate = task.getDueDate();
        taskInfo.createTime = task.getCreateTime();
        taskInfo.processName = findProcessName(task.getProcessDefinitionId());
        return taskInfo;
    }

    private String findProcessName(String processDefinitionId) {
        return processEngine
                .getRepositoryService()
                .createProcessDefinitionQuery()
                .processDefinitionId(processDefinitionId)
                .singleResult()
                .getName();
    }

    public void claim(String taskId, String userId, List<String> userGroups) {
        verifyTaskExists(taskId);
        Rights rights = getRightsToTask(taskId, userId, userGroups);
        if (!rights.canClaim())
            throw new ClaimForbiddenException(taskId);
        processEngine.getTaskService().claim(taskId, userId);
    }

    public void unclaim(String taskId, String userId, List<String> userGroups) {
        verifyTaskExists(taskId);
        Rights rights = getRightsToTask(taskId, userId, userGroups);
        if (!rights.canUnclaim())
            throw new UnclaimForbiddenException(taskId);
        processEngine.getTaskService().unclaim(taskId);
    }

    public Rights getRightsToTask(String taskId, String userId, List<String> userGroups) {
        return new ActivitiUserRights(processEngine, userId, userGroups).toTask(taskId);
    }

    public void verifyTaskExists(String taskId) {
        if (!taskExists(taskId))
            throw new NoSuchTaskException(taskId);
    }

    private Boolean taskExists(String taskId) {
        return Optional.ofNullable(
                processEngine
                        .getTaskService()
                .createTaskQuery()
                .active()
                .taskId(taskId)
                        .singleResult()
        ).isPresent();
    }
}
