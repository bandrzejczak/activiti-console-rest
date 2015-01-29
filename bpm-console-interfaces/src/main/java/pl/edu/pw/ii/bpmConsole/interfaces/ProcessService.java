package pl.edu.pw.ii.bpmConsole.interfaces;

import pl.edu.pw.ii.bpmConsole.valueObjects.ProcessDefinitionInfo;

import java.util.Collection;
import java.util.List;

public interface ProcessService {

    Collection<ProcessDefinitionInfo> listStartableProcesses(String userId, List<String> groups);

}
