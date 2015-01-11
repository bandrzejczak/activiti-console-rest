package pl.edu.pw.ii.bpmConsole.activiti.task;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;
import pl.edu.pw.ii.bpmConsole.interfaces.exceptions.ClaimForbiddenException;
import pl.edu.pw.ii.bpmConsole.interfaces.exceptions.NoSuchTaskException;
import pl.edu.pw.ii.bpmConsole.valueObjects.TaskInfo;

import java.util.List;
import java.util.stream.Collectors;

public class ActivitiTasks {
    private final TaskService taskService;
    private final RepositoryService repositoryService;

    public ActivitiTasks(TaskService taskService, RepositoryService repositoryService) {
        this.taskService = taskService;
        this.repositoryService = repositoryService;
    }

    public List<TaskInfo> listAssignedTo(String userId) {
        return mapTasks(
                taskService
                        .createTaskQuery()
                        .active()
                        .taskAssignee(userId)
                        .list()
        );
    }

    public List<TaskInfo> listAvailableFor(String userId, List<String> userGroups) {
        return mapTasks(
                taskService
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

    private TaskInfo mapTask(Task task) {
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
        return repositoryService
                .createProcessDefinitionQuery()
                .processDefinitionId(processDefinitionId)
                .singleResult()
                .getName();
    }

    public void claim(String taskId, String userId, List<String> userGroups) {
        verifyClaim(taskId, userId, userGroups);
        taskService.claim(taskId, userId);
    }

    private void verifyClaim(String taskId, String userId, List<String> userGroups) {
        if (!taskExists(taskId))
            throw new NoSuchTaskException(taskId);
        if (!canClaim(taskId, userId, userGroups))
            throw new ClaimForbiddenException(taskId);
    }

    private Boolean canClaim(String taskId, String userId, List<String> userGroups) {
        return listAvailableFor(userId, userGroups)
                .stream()
                .anyMatch(t -> t.id.equals(taskId));
    }

    private Boolean taskExists(String taskId) {
        return taskService
                .createTaskQuery()
                .active()
                .taskId(taskId)
                .singleResult() != null;
    }

}
