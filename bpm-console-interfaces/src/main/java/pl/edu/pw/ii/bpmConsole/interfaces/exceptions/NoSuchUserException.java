package pl.edu.pw.ii.bpmConsole.interfaces.exceptions;

import org.eclipse.jetty.http.HttpStatus;

public class NoSuchUserException extends ProcessEngineException {
    public NoSuchUserException(String userId) {
        super("There is no user with userId=" + userId, HttpStatus.NOT_FOUND_404);
    }
}
