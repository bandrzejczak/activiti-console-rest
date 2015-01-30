package pl.edu.pw.ii.bpmConsole.interfaces.exceptions;

public class WrongPasswordException extends ProcessEngineException {
    public WrongPasswordException() {
        super("User's current password doesn't match supplied password");
    }
}
