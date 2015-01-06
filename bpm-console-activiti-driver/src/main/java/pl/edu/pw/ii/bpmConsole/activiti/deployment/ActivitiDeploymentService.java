package pl.edu.pw.ii.bpmConsole.activiti.deployment;

import org.activiti.engine.ProcessEngine;
import pl.edu.pw.ii.bpmConsole.interfaces.Deployment;
import pl.edu.pw.ii.bpmConsole.interfaces.DeploymentService;
import pl.edu.pw.ii.bpmConsole.valueObjects.DeploymentInfo;
import pl.edu.pw.ii.bpmConsole.valueObjects.File;

import java.util.List;

public class ActivitiDeploymentService implements DeploymentService {

    private final ProcessEngine processEngine;

    public ActivitiDeploymentService(ProcessEngine processEngine) {
        this.processEngine = processEngine;
    }

    @Override
    public Deployment create(File deployment) {
        return new ActivitiDeployment(processEngine.getRepositoryService(), deployment);
    }

    @Override
    public List<DeploymentInfo> list() {
        return new ActivitiDeployments(processEngine.getRepositoryService()).toList();
    }
}
