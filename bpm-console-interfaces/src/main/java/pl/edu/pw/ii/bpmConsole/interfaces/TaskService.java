package pl.edu.pw.ii.bpmConsole.interfaces;

import pl.edu.pw.ii.bpmConsole.valueObjects.TaskInfo;

import java.util.List;

public interface TaskService {

    List<TaskInfo> listAssignedTo(String userId);

    List<TaskInfo> listAvailableFor(String userId, List<String> userGroups);
}