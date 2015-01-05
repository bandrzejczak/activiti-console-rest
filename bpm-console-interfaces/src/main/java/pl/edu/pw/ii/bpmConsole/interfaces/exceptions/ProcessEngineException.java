package pl.edu.pw.ii.bpmConsole.interfaces.exceptions;

public class ProcessEngineException extends RuntimeException{

    ProcessEngineException(String message){
        super(message);
    }

    ProcessEngineException(String message, Throwable cause){
        super(message, cause);
    }

}
