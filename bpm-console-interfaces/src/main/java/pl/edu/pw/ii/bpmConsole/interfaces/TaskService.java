package pl.edu.pw.ii.bpmConsole.interfaces;

import pl.edu.pw.ii.bpmConsole.valueObjects.AuthUser;
import pl.edu.pw.ii.bpmConsole.valueObjects.FormInfo;
import pl.edu.pw.ii.bpmConsole.valueObjects.TaskInfo;

import java.util.List;
import java.util.Map;

public interface TaskService {

    List<TaskInfo> listAssignedTo(String userId);

    List<TaskInfo> listAvailableFor(AuthUser user);

    void claim(String taskId, AuthUser user);

    void unclaim(String taskId, AuthUser user);

    void submit(String taskId, String userId, Map<String, String> properties);

    FormInfo findFormForTask(String taskId, AuthUser user);
}
