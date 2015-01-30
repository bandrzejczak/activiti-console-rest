package pl.edu.pw.ii.bpmConsole.interfaces.exceptions;

import org.eclipse.jetty.http.HttpStatus;

public class NoSuchGroupException extends ProcessEngineException {
    public NoSuchGroupException(String groupId) {
        super("There is no group with groupId=" + groupId, HttpStatus.NOT_FOUND_404);
    }
}
