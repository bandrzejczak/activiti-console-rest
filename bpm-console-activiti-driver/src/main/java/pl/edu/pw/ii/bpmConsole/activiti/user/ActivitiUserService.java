package pl.edu.pw.ii.bpmConsole.activiti.user;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.identity.Group;
import pl.edu.pw.ii.bpmConsole.interfaces.UserService;

import java.util.List;
import java.util.stream.Collectors;

public class ActivitiUserService implements UserService {

    private final ProcessEngine processEngine;

    public ActivitiUserService(ProcessEngine processEngine) {
        this.processEngine = processEngine;
    }

    //TODO delete it!
    @Override
    public List<String> getUserGroups(String username) {
        return processEngine
                .getIdentityService()
                .createGroupQuery()
                .groupMember(username)
                .list()
                .stream()
                .map(Group::getId)
                .collect(Collectors.toList());
    }

    //TODO delete it!
    @Override
    public boolean checkPassword(String username, String password) {
        return processEngine
                .getIdentityService()
                .checkPassword(username, password);
    }

    //TODO delete it!
    @Override
    public void createUser(String login, String password) {
        org.activiti.engine.identity.User activitiUser = processEngine.getIdentityService().newUser(login);
        activitiUser.setPassword(password);
        processEngine.getIdentityService().saveUser(activitiUser);
    }

    //TODO delete it!
    @Override
    public void createGroup(String name) {
        Group group = processEngine.getIdentityService().newGroup(name);
        processEngine.getIdentityService().saveGroup(group);
    }

    //TODO delete it!
    @Override
    public void createMembership(String login, String groupName) {
        processEngine.getIdentityService().createMembership(login, groupName);
    }
}
