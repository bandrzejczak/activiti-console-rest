package pl.edu.pw.ii.bpmConsole.valueObjects;

public class UnzippingException extends RuntimeException {
    public UnzippingException(Throwable cause) {
        super("There was a problem while unzipping the archive", cause);
    }
}
