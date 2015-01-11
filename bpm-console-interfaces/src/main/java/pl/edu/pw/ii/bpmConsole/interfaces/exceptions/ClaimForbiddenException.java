package pl.edu.pw.ii.bpmConsole.interfaces.exceptions;

public class ClaimForbiddenException extends ProcessEngineException {
    public ClaimForbiddenException(String taskId) {
        super("Logged in user cannot claim task with id=" + taskId);
    }
}
