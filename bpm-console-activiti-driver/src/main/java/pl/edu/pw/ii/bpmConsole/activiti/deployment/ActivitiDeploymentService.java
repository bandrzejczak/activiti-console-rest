package pl.edu.pw.ii.bpmConsole.activiti.deployment;

import org.activiti.engine.RepositoryService;
import pl.edu.pw.ii.bpmConsole.interfaces.DeploymentService;
import pl.edu.pw.ii.bpmConsole.valueObjects.DeploymentInfo;
import pl.edu.pw.ii.bpmConsole.valueObjects.File;

import java.util.List;

public class ActivitiDeploymentService implements DeploymentService {

    private final RepositoryService repositoryService;

    public ActivitiDeploymentService(RepositoryService repositoryService) {
        this.repositoryService = repositoryService;
    }

    @Override
    public void deploy(File deployment) {
        new ActivitiDeployment(repositoryService, deployment).deploy();
    }

    @Override
    public List<DeploymentInfo> list() {
        return new ActivitiDeployments(repositoryService).toList();
    }

    @Override
    public void delete(String processDefinitionId) {
        new ActivitiDeployments(repositoryService).remove(processDefinitionId);
    }
}
