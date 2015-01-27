package pl.edu.pw.ii.bpmConsole.interfaces.exceptions;

import org.eclipse.jetty.http.HttpStatus;

public class ProcessEngineException extends RuntimeException{

    public Integer code = HttpStatus.INTERNAL_SERVER_ERROR_500;

    ProcessEngineException(String message){
        super(message);
    }

    ProcessEngineException(String message, Throwable cause){
        super(message, cause);
    }

    ProcessEngineException(String message, Integer code) {
        super(message);
        this.code = code;
    }

}
