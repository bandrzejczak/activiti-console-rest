package pl.edu.pw.ii.bpmConsole.interfaces.exceptions;

import org.eclipse.jetty.http.HttpStatus;

public class ClaimForbiddenException extends ProcessEngineException {
    public ClaimForbiddenException(String taskId) {
        super("Logged in user cannot claim task with id=" + taskId, HttpStatus.FORBIDDEN_403);
    }
}
