package pl.edu.pw.ii.BpmConsole.Interfaces.exceptions;

import java.sql.SQLException;

public class ProcessEngineDatabaseException extends RuntimeException {

    public ProcessEngineDatabaseException(SQLException e) {
        super(e);
    }

}
