package pl.edu.pw.ii.bpmConsole.interfaces.exceptions;

import java.sql.SQLException;

public class ProcessEngineDatabaseException extends RuntimeException {

    public ProcessEngineDatabaseException(SQLException e) {
        super(e);
    }

}
