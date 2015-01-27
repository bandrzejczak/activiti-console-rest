package pl.edu.pw.ii.bpmConsole.interfaces.exceptions;

import org.eclipse.jetty.http.HttpStatus;

public class AccessDeniedException extends ProcessEngineException {
    public AccessDeniedException(String taskId) {
        super("User has no rights to view task with taskId=" + taskId, HttpStatus.FORBIDDEN_403);
    }
}
