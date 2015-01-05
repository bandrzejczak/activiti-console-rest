package pl.edu.pw.ii.bpmConsole.interfaces.exceptions;

public class EmptyDeploymentException extends ProcessEngineException {
    public EmptyDeploymentException() {
        super("Deployed process has no data");
    }
}
