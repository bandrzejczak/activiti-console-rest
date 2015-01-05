package pl.edu.pw.ii.bpmConsole.interfaces;

public interface Deployment {
    Integer MAX_FILE_SIZE = 3 * 1024 * 1024; //3MB

    void deploy();
}
