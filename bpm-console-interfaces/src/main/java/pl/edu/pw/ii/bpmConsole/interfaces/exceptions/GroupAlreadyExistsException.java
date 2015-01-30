package pl.edu.pw.ii.bpmConsole.interfaces.exceptions;

import org.eclipse.jetty.http.HttpStatus;

public class GroupAlreadyExistsException extends ProcessEngineException {
    public GroupAlreadyExistsException(String id) {
        super("Group with id=" + id + " already exists", HttpStatus.CONFLICT_409);
    }
}
