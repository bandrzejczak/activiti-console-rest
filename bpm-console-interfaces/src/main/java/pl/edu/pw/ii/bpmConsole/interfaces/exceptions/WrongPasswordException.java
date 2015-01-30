package pl.edu.pw.ii.bpmConsole.interfaces.exceptions;

import org.eclipse.jetty.http.HttpStatus;

public class WrongPasswordException extends ProcessEngineException {
    public WrongPasswordException() {
        super("User's current password doesn't match supplied password", HttpStatus.FORBIDDEN_403);
    }
}
