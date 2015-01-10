package pl.edu.pw.ii.bpmConsole.activiti.task;

import org.activiti.engine.RepositoryService;
import pl.edu.pw.ii.bpmConsole.interfaces.TaskService;
import pl.edu.pw.ii.bpmConsole.valueObjects.TaskInfo;

import java.util.List;

public class ActivitiTaskService implements TaskService {

    private final org.activiti.engine.TaskService taskService;
    private final RepositoryService repositoryService;

    public ActivitiTaskService(org.activiti.engine.TaskService taskService, RepositoryService repositoryService) {
        this.taskService = taskService;
        this.repositoryService = repositoryService;
    }

    @Override
    public List<TaskInfo> list(String userId) {
        return new ActivitiTasks(taskService, repositoryService).listAssignedTo(userId);
    }
}
