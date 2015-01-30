package pl.edu.pw.ii.bpmConsole.activiti.task;

import org.activiti.engine.ProcessEngine;
import pl.edu.pw.ii.bpmConsole.interfaces.TaskService;
import pl.edu.pw.ii.bpmConsole.valueObjects.AuthUser;
import pl.edu.pw.ii.bpmConsole.valueObjects.FormInfo;
import pl.edu.pw.ii.bpmConsole.valueObjects.TaskInfo;

import java.util.List;
import java.util.Map;

public class ActivitiTaskService implements TaskService {

    private final ProcessEngine processEngine;

    public ActivitiTaskService(ProcessEngine processEngine) {
        this.processEngine = processEngine;
    }

    @Override
    public List<TaskInfo> listAssignedTo(String userId) {
        return new ActivitiTasks(processEngine).listAssignedTo(userId);
    }

    @Override
    public List<TaskInfo> listAvailableFor(AuthUser user) {
        return new ActivitiTasks(processEngine).listAvailableFor(user);
    }

    @Override
    public void claim(String taskId, AuthUser user) {
        new ActivitiTasks(processEngine).claim(taskId, user);
    }

    @Override
    public void unclaim(String taskId, AuthUser user) {
        new ActivitiTasks(processEngine).unclaim(taskId, user);
    }

    @Override
    public void submit(String taskId, String userId, Map<String, String> properties) {
        new ActivitiForm(processEngine).submit(taskId, userId, properties);
    }

    @Override
    public FormInfo findFormForTask(String taskId, AuthUser user) {
        return new ActivitiForm(processEngine).findFormForTask(taskId, user);
    }
}
