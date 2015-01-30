package pl.edu.pw.ii.bpmConsole.interfaces.exceptions;

import org.eclipse.jetty.http.HttpStatus;

public class ProcessDefinitionDoesntExistsException extends ProcessEngineException {
    public ProcessDefinitionDoesntExistsException(String processDefinitionId) {
        super("Process definition with id " + processDefinitionId + " doesn't exist!", HttpStatus.NOT_FOUND_404);
    }
}
