package pl.edu.pw.ii.bpmConsole.activiti.task;

import org.activiti.engine.ProcessEngine;
import pl.edu.pw.ii.bpmConsole.interfaces.TaskService;
import pl.edu.pw.ii.bpmConsole.valueObjects.FormInfo;
import pl.edu.pw.ii.bpmConsole.valueObjects.Rights;
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
    public List<TaskInfo> listAvailableFor(String userId, List<String> userGroups) {
        return new ActivitiTasks(processEngine).listAvailableFor(userId, userGroups);
    }

    @Override
    public void claim(String taskId, String userId, List<String> userGroups) {
        new ActivitiTasks(processEngine).claim(taskId, userId, userGroups);
    }

    @Override
    public void unclaim(String taskId, String userId, List<String> userGroups) {
        new ActivitiTasks(processEngine).unclaim(taskId, userId, userGroups);
    }

    @Override
    public void submit(String taskId, String userId, Map<String, String> properties) {
        new ActivitiForm(processEngine).submit(taskId, userId, properties);
    }

    @Override
    public FormInfo findFormForTask(String taskId, Rights rightsToTask) {
        return new ActivitiForm(processEngine).findFormForTask(taskId, rightsToTask);
    }
}
