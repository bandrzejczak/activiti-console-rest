package pl.edu.pw.ii.bpmConsole.interfaces.exceptions;

public class NoSuchTaskException extends ProcessEngineException {
    public NoSuchTaskException(String taskId) {
        super("There is no task with id=" + taskId);
    }
}
