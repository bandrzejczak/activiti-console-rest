package pl.edu.pw.ii.bpmConsole.interfaces.exceptions;

public class ZipFileHasNoProcessesException extends ProcessEngineException {
    public ZipFileHasNoProcessesException(){
        super("Uploaded zip file has no deployable process inside");
    }

}
