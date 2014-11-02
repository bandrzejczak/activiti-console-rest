package pl.edu.pw.ii.BpmConsole.Interfaces;

import java.util.List;

public interface ProcessEngine {

    public void init();

    List<String> getUserGroups(String username);

    boolean checkPassword(String username, String password);

    void createUser(String login, String password);

    void createGroup(String login);

    void createMembership(String login, String groupName);
}
