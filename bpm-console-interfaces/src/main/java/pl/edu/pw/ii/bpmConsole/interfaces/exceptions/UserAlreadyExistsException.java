package pl.edu.pw.ii.bpmConsole.interfaces.exceptions;

import org.eclipse.jetty.http.HttpStatus;

public class UserAlreadyExistsException extends ProcessEngineException {
    public UserAlreadyExistsException(String id) {
        super("User with id=" + id + " already exists", HttpStatus.CONFLICT_409);
    }
}
