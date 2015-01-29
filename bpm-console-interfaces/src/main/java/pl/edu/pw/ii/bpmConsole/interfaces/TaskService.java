package pl.edu.pw.ii.bpmConsole.interfaces;

import pl.edu.pw.ii.bpmConsole.valueObjects.FormInfo;
import pl.edu.pw.ii.bpmConsole.valueObjects.Rights;
import pl.edu.pw.ii.bpmConsole.valueObjects.TaskInfo;

import java.util.List;

public interface TaskService {

    List<TaskInfo> listAssignedTo(String userId);

    List<TaskInfo> listAvailableFor(String userId, List<String> userGroups);

    void claim(String taskId, String userId, List<String> userGroups);

    void unclaim(String taskId, String id, List<String> groups);

    FormInfo findFormForTask(String taskId, Rights rightsToTask);
}
