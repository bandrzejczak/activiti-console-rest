package pl.edu.pw.ii.bpmConsole.interfaces.exceptions;

public class ProcessDeploymentFailedException extends ProcessEngineException {
    public ProcessDeploymentFailedException() {
        super("Process file couldn't be deployed");
    }
}
