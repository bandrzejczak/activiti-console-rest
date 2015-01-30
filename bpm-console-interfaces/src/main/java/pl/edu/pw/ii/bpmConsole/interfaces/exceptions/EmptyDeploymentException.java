package pl.edu.pw.ii.bpmConsole.interfaces.exceptions;

import org.eclipse.jetty.http.HttpStatus;

public class EmptyDeploymentException extends ProcessEngineException {
    public EmptyDeploymentException() {
        super("Deployed process has no data", HttpStatus.BAD_REQUEST_400);
    }
}
