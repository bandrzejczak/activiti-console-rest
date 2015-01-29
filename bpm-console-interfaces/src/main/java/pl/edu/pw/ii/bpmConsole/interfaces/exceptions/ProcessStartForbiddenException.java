package pl.edu.pw.ii.bpmConsole.interfaces.exceptions;

import org.eclipse.jetty.http.HttpStatus;

public class ProcessStartForbiddenException extends ProcessEngineException {
    public ProcessStartForbiddenException(String processDefinitionId) {
        super("Logged in user cannot start process with id " + processDefinitionId, HttpStatus.FORBIDDEN_403);
    }
}
