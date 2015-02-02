package pl.edu.pw.ii.bpmConsole.activiti.user;

import org.activiti.engine.ProcessEngine;
import pl.edu.pw.ii.bpmConsole.interfaces.UserRights;
import pl.edu.pw.ii.bpmConsole.interfaces.UserService;
import pl.edu.pw.ii.bpmConsole.valueObjects.AuthUser;
import pl.edu.pw.ii.bpmConsole.valueObjects.GroupInfo;
import pl.edu.pw.ii.bpmConsole.valueObjects.UserInfo;

import java.util.List;

public class ActivitiUserService implements UserService {

    private final ProcessEngine processEngine;

    public ActivitiUserService(ProcessEngine processEngine) {
        this.processEngine = processEngine;
    }

    @Override
    public boolean validateCredentials(String userId, String password) {
        return new ActivitiUsers(processEngine.getIdentityService()).validateCredentials(userId, password);
    }

    @Override
    public UserRights verifyRights(AuthUser user) {
        return new ActivitiUserRights(processEngine, user);
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
    public void createUser(UserInfo userInfo, Boolean isAdmin) {
        new ActivitiUsers(processEngine.getIdentityService()).edit(userInfo, null, isAdmin);
    }

    @Override
    public void editUser(UserInfo userInfo, String userId, Boolean isAdmin) {
        new ActivitiUsers(processEngine.getIdentityService()).edit(userInfo, userId, isAdmin);
    }

    @Override
    public void createGroup(GroupInfo groupInfo) {
        new ActivitiGroups(processEngine.getIdentityService()).edit(groupInfo, null);
    }

    @Override
    public void editGroup(GroupInfo groupInfo, String groupId) {
        new ActivitiGroups(processEngine.getIdentityService()).edit(groupInfo, groupId);
    }

    @Override
    public void deleteUser(String userToDeleteId, String currentUserId) {
        new ActivitiUsers(processEngine.getIdentityService()).delete(userToDeleteId, currentUserId);
    }

    @Override
    public void deleteGroup(String groupId) {
        new ActivitiGroups(processEngine.getIdentityService()).delete(groupId);
    }

    @Override
    public void addMembership(String groupId, String userId) {
        new ActivitiGroups(processEngine.getIdentityService()).addMembership(groupId, userId);
    }

    @Override
    public List<GroupInfo> getUserGroups(String userId) {
        return new ActivitiGroups(processEngine.getIdentityService()).forUser(userId);
    }

    @Override
    public void deleteMembership(String groupId, String userId) {
        new ActivitiGroups(processEngine.getIdentityService()).deleteMembership(groupId, userId);
    }
}
