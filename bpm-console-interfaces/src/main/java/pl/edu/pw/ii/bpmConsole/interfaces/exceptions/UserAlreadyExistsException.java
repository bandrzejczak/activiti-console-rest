package pl.edu.pw.ii.bpmConsole.interfaces.exceptions;

public class UserAlreadyExistsException extends ProcessEngineException {
    public UserAlreadyExistsException(String id) {
        super("User with id=" + id + " already exists");
    }
}
