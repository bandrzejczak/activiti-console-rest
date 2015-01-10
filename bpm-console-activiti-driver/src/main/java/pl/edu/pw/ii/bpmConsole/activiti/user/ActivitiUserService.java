package pl.edu.pw.ii.bpmConsole.activiti.user;

import org.activiti.engine.IdentityService;
import org.activiti.engine.identity.Group;
import pl.edu.pw.ii.bpmConsole.interfaces.UserService;

import java.util.List;
import java.util.stream.Collectors;

public class ActivitiUserService implements UserService {

    private final IdentityService identityService;

    public ActivitiUserService(IdentityService identityService) {
        this.identityService = identityService;
    }

    //TODO delete it!
    @Override
    public List<String> getUserGroups(String username) {
        return identityService
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
        return identityService
                .checkPassword(username, password);
    }

    //TODO delete it!
    @Override
    public void createUser(String login, String password) {
        org.activiti.engine.identity.User activitiUser = identityService.newUser(login);
        activitiUser.setPassword(password);
        identityService.saveUser(activitiUser);
    }

    //TODO delete it!
    @Override
    public void createGroup(String name) {
        Group group = identityService.newGroup(name);
        identityService.saveGroup(group);
    }

    //TODO delete it!
    @Override
    public void createMembership(String login, String groupName) {
        identityService.createMembership(login, groupName);
    }
}
