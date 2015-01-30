package pl.edu.pw.ii.bpmConsole.activiti.user;

import org.activiti.engine.ProcessEngine;
import pl.edu.pw.ii.bpmConsole.interfaces.UserRights;
import pl.edu.pw.ii.bpmConsole.interfaces.UserService;
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

    @Override
    public void createGroup(GroupInfo groupInfo) {
        new ActivitiGroups(processEngine.getIdentityService()).edit(groupInfo, null);
    }

    @Override
    public void editGroup(GroupInfo groupInfo, String groupId) {
        new ActivitiGroups(processEngine.getIdentityService()).edit(groupInfo, groupId);
    }

    @Override
    public void deleteUser(String userId) {
        new ActivitiUsers(processEngine.getIdentityService()).delete(userId);
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
    public List<String> getUserGroups(String userId) {
        return new ActivitiGroups(processEngine.getIdentityService()).forUser(userId);
    }

    @Override
    public void deleteMembership(String groupId, String userId) {
        new ActivitiGroups(processEngine.getIdentityService()).deleteMembership(groupId, userId);
    }
}
