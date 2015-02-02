package pl.edu.pw.ii.bpmConsole.interfaces.exceptions;

import org.eclipse.jetty.http.HttpStatus;

public class CannotDeleteOwnAccountException extends ProcessEngineException {
    public CannotDeleteOwnAccountException() {
        super("Cannot delete user account, that you're currently logged in!", HttpStatus.FORBIDDEN_403);
    }
}
