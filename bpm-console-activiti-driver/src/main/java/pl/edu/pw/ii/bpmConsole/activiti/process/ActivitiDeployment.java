package pl.edu.pw.ii.bpmConsole.activiti.process;

import org.activiti.engine.RepositoryService;
import pl.edu.pw.ii.bpmConsole.interfaces.Deployment;
import pl.edu.pw.ii.bpmConsole.valueObjects.File;
import pl.edu.pw.ii.bpmConsole.valueObjects.ZipFile;

import java.io.ByteArrayInputStream;

public class ActivitiDeployment implements Deployment {

    private final RepositoryService repositoryService;
    private final File deployment;
    public static final String[] ALLOWED_EXTENSIONS = {".bpmn", ".bpmn20.xml"};

    public ActivitiDeployment(RepositoryService repositoryService, File deployment) {
        this.repositoryService = repositoryService;
        this.deployment = deployment;
    }

    public void deploy() {
        if (deployment.isZip())
            deployZip();
        else
            deploySingleProcess(deployment);
    }

    private void deployZip() {
        for (File singleProcess : new ZipFile(deployment))
            deploySingleProcess(singleProcess);
    }

    private void deploySingleProcess(File process) {
        if (isAllowedExtension(process.filename))
            repositoryService
                    .createDeployment()
                    .name(process.filename)
                    .addInputStream(process.filename, new ByteArrayInputStream(process.getContent()))
                    .deploy();
    }

    private Boolean isAllowedExtension(String fileName) {
        for (String extension : ALLOWED_EXTENSIONS)
            if (fileName.endsWith(extension))
                return true;
        return false;
    }
}
