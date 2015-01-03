package pl.edu.pw.ii.bpmConsole.interfaces;

import pl.edu.pw.ii.bpmConsole.valueObjects.File;

import java.util.List;

public interface ProcessEngine {

    void init();

    List<String> getUserGroups(String username);

    boolean checkPassword(String username, String password);

    void createUser(String login, String password);

    void createGroup(String login);

    void createMembership(String login, String groupName);

    Deployment createDeployment(File deployment);

    List<String> listDeployments();
}
