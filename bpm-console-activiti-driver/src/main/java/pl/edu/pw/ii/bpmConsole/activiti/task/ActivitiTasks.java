package pl.edu.pw.ii.bpmConsole.activiti.task;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.task.Task;
import pl.edu.pw.ii.bpmConsole.activiti.user.ActivitiUserRights;
import pl.edu.pw.ii.bpmConsole.interfaces.exceptions.ClaimForbiddenException;
import pl.edu.pw.ii.bpmConsole.interfaces.exceptions.NoSuchTaskException;
import pl.edu.pw.ii.bpmConsole.interfaces.exceptions.UnclaimForbiddenException;
import pl.edu.pw.ii.bpmConsole.valueObjects.AuthUser;
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

    public List<TaskInfo> listAvailableFor(AuthUser user) {
        return mapTasks(
                processEngine
                        .getTaskService()
                        .createTaskQuery()
                        .active()
                        .or()
                        .taskCandidateUser(user.id)
                        .taskCandidateGroupIn(user.groups)
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

    public void claim(String taskId, AuthUser user) {
        verifyTaskExists(taskId);
        Rights rights = getRightsToTask(taskId, user);
        if (!rights.canClaim())
            throw new ClaimForbiddenException(taskId);
        processEngine.getTaskService().claim(taskId, user.id);
    }

    public void unclaim(String taskId, AuthUser user) {
        verifyTaskExists(taskId);
        Rights rights = getRightsToTask(taskId, user);
        if (!rights.canUnclaim())
            throw new UnclaimForbiddenException(taskId);
        processEngine.getTaskService().unclaim(taskId);
    }

    public Rights getRightsToTask(String taskId, AuthUser user) {
        return new ActivitiUserRights(processEngine, user).toTask(taskId);
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
