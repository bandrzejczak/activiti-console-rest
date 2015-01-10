package pl.edu.pw.ii.bpmConsole.interfaces;

public interface ProcessEngine {

    void init();

    UserService userService();

    DeploymentService deploymentService();

    TaskService taskService();
}
