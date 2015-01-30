package pl.edu.pw.ii.bpmConsole.interfaces.exceptions;

import org.eclipse.jetty.http.HttpStatus;

public class ZipFileHasNoProcessesException extends ProcessEngineException {
    public ZipFileHasNoProcessesException(){
        super("Uploaded zip file has no deployable process inside", HttpStatus.BAD_REQUEST_400);
    }

}
