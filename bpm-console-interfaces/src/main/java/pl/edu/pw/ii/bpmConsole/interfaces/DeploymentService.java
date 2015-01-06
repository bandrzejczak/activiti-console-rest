package pl.edu.pw.ii.bpmConsole.interfaces;

import pl.edu.pw.ii.bpmConsole.valueObjects.DeploymentInfo;
import pl.edu.pw.ii.bpmConsole.valueObjects.File;

import java.util.List;

public interface DeploymentService {

    Deployment create(File deployment);

    List<DeploymentInfo> list();
}
