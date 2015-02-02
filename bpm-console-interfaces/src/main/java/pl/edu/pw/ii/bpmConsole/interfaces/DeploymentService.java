package pl.edu.pw.ii.bpmConsole.interfaces;

import pl.edu.pw.ii.bpmConsole.valueObjects.DeploymentInfo;
import pl.edu.pw.ii.bpmConsole.valueObjects.File;

import java.util.List;

public interface DeploymentService {

    void deploy(File deployment);

    List<DeploymentInfo> list();

    void delete(String processDefinitionId);
}
