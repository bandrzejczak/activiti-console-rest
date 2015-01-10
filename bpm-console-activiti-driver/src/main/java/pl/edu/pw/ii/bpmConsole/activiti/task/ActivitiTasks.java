package pl.edu.pw.ii.bpmConsole.activiti.task;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;
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
        return taskService
                .createTaskQuery()
                .active()
                .taskAssignee(userId)
                .list()
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
}
