package pl.edu.pw.ii.bpmConsole.interfaces.exceptions;

import org.eclipse.jetty.http.HttpStatus;

public class UnclaimForbiddenException extends ProcessEngineException {
    public UnclaimForbiddenException(String taskId) {
        sup\er("Logged in user cannot unclaim task with id=" + taskId, HttpStatus.FORBIDDEN_403);
    }
}
