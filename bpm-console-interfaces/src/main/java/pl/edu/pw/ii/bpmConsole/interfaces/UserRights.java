package pl.edu.pw.ii.bpmConsole.interfaces;

import pl.edu.pw.ii.bpmConsole.valueObjects.Rights;

public interface UserRights {
    Rights toTask(String taskId);

    void toUser(String userId);
}
