package pl.edu.pw.ii.bpmConsole.interfaces.exceptions;

import org.eclipse.jetty.http.HttpStatus;

public class NoSuchTaskException extends ProcessEngineException {
    public NoSuchTaskException(String taskId) {
        super("There is no task with id=" + taskId, HttpStatus.NOT_FOUND_404);
    }
}
