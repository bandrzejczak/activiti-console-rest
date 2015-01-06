package pl.edu.pw.ii.bpmConsole.interfaces;

public interface Deployment {
    Integer MAX_FILE_SIZE = 3 * 1024 * 1024; //3MB
    String[] ALLOWED_EXTENSIONS = {".bpmn", ".bpmn20.xml"};

    void deploy();
}
