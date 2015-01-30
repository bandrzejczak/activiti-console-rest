package pl.edu.pw.ii.bpmConsole.interfaces.exceptions;

public class GroupAlreadyExistsException extends ProcessEngineException {
    public GroupAlreadyExistsException(String id) {
        super("Group with id=" + id + " already exists");
    }
}
