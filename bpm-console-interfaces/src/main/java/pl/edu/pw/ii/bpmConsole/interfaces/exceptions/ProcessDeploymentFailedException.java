package pl.edu.pw.ii.bpmConsole.interfaces.exceptions;

public class ProcessDeploymentFailedException extends ProcessEngineException {
    public ProcessDeploymentFailedException() {
        super("Process file couldn't be deployed");
    }

    public ProcessDeploymentFailedException(Throwable cause) {
        super("Process file couldn't be deployed", cause);
    }
}
