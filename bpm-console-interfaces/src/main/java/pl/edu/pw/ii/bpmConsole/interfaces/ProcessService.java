package pl.edu.pw.ii.bpmConsole.interfaces;

import pl.edu.pw.ii.bpmConsole.valueObjects.AuthUser;
import pl.edu.pw.ii.bpmConsole.valueObjects.ProcessDefinitionInfo;
import pl.edu.pw.ii.bpmConsole.valueObjects.ProcessInstanceInfo;

import java.util.Collection;
import java.util.List;

public interface ProcessService {

    Collection<ProcessDefinitionInfo> listStartableProcesses(AuthUser user);

    void startProcess(String processDefinitionId, AuthUser user);

    List<ProcessInstanceInfo> listProcessInstances();
}
