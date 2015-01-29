package pl.edu.pw.ii.bpmConsole.interfaces.exceptions;

import org.eclipse.jetty.http.HttpStatus;

public class NoSuchProcessException extends ProcessEngineException {
    public NoSuchProcessException(String processDefinitionId) {
        super("There is no process with id=" + processDefinitionId, HttpStatus.NOT_FOUND_404);
    }
}
