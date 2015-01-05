package pl.edu.pw.ii.bpmConsole.activiti.process;

import org.activiti.engine.RepositoryService;
import pl.edu.pw.ii.bpmConsole.interfaces.Deployment;
import pl.edu.pw.ii.bpmConsole.interfaces.exceptions.DeploymentTooBigException;
import pl.edu.pw.ii.bpmConsole.interfaces.exceptions.EmptyDeploymentException;
import pl.edu.pw.ii.bpmConsole.interfaces.exceptions.ProcessDeploymentFailedException;
import pl.edu.pw.ii.bpmConsole.interfaces.exceptions.ZipFileHasNoProcessesException;
import pl.edu.pw.ii.bpmConsole.valueObjects.File;
import pl.edu.pw.ii.bpmConsole.valueObjects.ZipFile;

import java.io.ByteArrayInputStream;
import java.util.Optional;

public class ActivitiDeployment implements Deployment {

    private final RepositoryService repositoryService;
    private final File deployment;
    public static final String[] ALLOWED_EXTENSIONS = {".bpmn", ".bpmn20.xml"};

    public ActivitiDeployment(RepositoryService repositoryService, File deployment) {
        verifySize(deployment);
        this.repositoryService = repositoryService;
        this.deployment = deployment;
    }

    private void verifySize(File deployment) {
        if(deployment.fileSize == 0)
            throw new EmptyDeploymentException();
        if(deployment.fileSize > Deployment.MAX_FILE_SIZE)
            throw new DeploymentTooBigException();
    }

    public void deploy() {
        if (deployment.isZip())
            deployZip();
        else
            deploySingleProcess(deployment)
                    .orElseGet(() -> { throw new ProcessDeploymentFailedException(); });
    }

    private void deployZip() {
        if(new ZipFile(deployment)
                .stream()
                .map(this::deploySingleProcess)
                .filter(Optional::isPresent)
                .count() == 0)
            throw new ZipFileHasNoProcessesException();
    }

    private Optional<org.activiti.engine.repository.Deployment> deploySingleProcess(File process) {
        if (isAllowedExtension(process.fileName))
            return Optional.ofNullable(repositoryService
                    .createDeployment()
                    .name(process.fileName)
                    .addInputStream(process.fileName, new ByteArrayInputStream(process.getContent()))
                    .deploy());
        else
            return Optional.empty();
    }

    private Boolean isAllowedExtension(String fileName) {
        for (String extension : ALLOWED_EXTENSIONS)
            if (fileName.endsWith(extension))
                return true;
        return false;
    }
}
