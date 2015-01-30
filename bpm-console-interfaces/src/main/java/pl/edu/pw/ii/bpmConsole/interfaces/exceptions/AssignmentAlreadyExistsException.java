package pl.edu.pw.ii.bpmConsole.interfaces.exceptions;

public class AssignmentAlreadyExistsException extends ProcessEngineException {
    public AssignmentAlreadyExistsException(String userId, String groupId) {
        super("User with userId=" + userId + " already is a member of group with groupId=" + groupId);
    }
}
