package pl.edu.pw.ii.bpmConsole.interfaces.exceptions;

import org.eclipse.jetty.http.HttpStatus;

public class NoSuchAssignmentException extends ProcessEngineException {
    public NoSuchAssignmentException(String userId, String groupId) {
        super("User with userId=" + userId + " is not a member of group with groupId=" + groupId,
                HttpStatus.NOT_FOUND_404);
    }
}
