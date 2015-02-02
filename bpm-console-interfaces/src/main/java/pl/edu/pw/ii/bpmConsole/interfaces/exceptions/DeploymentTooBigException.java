package pl.edu.pw.ii.bpmConsole.interfaces.exceptions;

import org.eclipse.jetty.http.HttpStatus;

public class DeploymentTooBigException extends ProcessEngineException {
    public DeploymentTooBigException() {
        super("Deployment file exceeds maximum size", HttpStatus.BAD_REQUEST_400);
    }
}
