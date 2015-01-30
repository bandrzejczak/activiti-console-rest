package pl.edu.pw.ii.bpmConsole.interfaces;

import pl.edu.pw.ii.bpmConsole.valueObjects.ProcessDefinitionInfo;
import pl.edu.pw.ii.bpmConsole.valueObjects.ProcessInstanceInfo;

import java.util.Collection;
import java.util.List;

public interface ProcessService {

    Collection<ProcessDefinitionInfo> listStartableProcesses(String userId, List<String> groups);

    void startProcess(String processDefinitionId, String userId, List<String> groups);

    List<ProcessInstanceInfo> listProcessInstances();
}
