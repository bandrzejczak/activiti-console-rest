package pl.edu.pw.ii.bpmConsole.activiti.user;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.identity.Group;
import pl.edu.pw.ii.bpmConsole.interfaces.UserRights;
import pl.edu.pw.ii.bpmConsole.interfaces.UserService;
import pl.edu.pw.ii.bpmConsole.valueObjects.GroupInfo;
import pl.edu.pw.ii.bpmConsole.valueObjects.UserInfo;

import java.util.List;
import java.util.stream.Collectors;

public class ActivitiUserService implements UserService {

    private final ProcessEngine processEngine;

    public ActivitiUserService(ProcessEngine processEngine) {
        this.processEngine = processEngine;
    }

    //TODO fix it!
    @Override
    public List<String> getUserGroups(String username) {
        return processEngine.getIdentityService()
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
        return processEngine.getIdentityService()
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

    @Override
    public UserRights verifyRights(String id, List<String> groups) {
        return new ActivitiUserRights(processEngine, id, groups);
    }

    @Override
    public List<UserInfo> listUsers() {
        return new ActivitiUsers(processEngine.getIdentityService()).list();
    }

    @Override
    public List<GroupInfo> listGroups() {
        return new ActivitiGroups(processEngine.getIdentityService()).list();
    }

    @Override
    public UserInfo getUser(String userId) {
        return new ActivitiUsers(processEngine.getIdentityService()).get(userId);
    }

    @Override
    public GroupInfo getGroup(String groupId) {
        return new ActivitiGroups(processEngine.getIdentityService()).get(groupId);
    }

    @Override
    public void createUser(UserInfo userInfo) {
        new ActivitiUsers(processEngine.getIdentityService()).edit(userInfo, null);
    }

    @Override
    public void editUser(UserInfo userInfo, String userId) {
        new ActivitiUsers(processEngine.getIdentityService()).edit(userInfo, userId);
    }
}
