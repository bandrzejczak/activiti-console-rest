package pl.edu.pw.ii.bpmConsole.interfaces;

import pl.edu.pw.ii.bpmConsole.valueObjects.AuthUser;
import pl.edu.pw.ii.bpmConsole.valueObjects.GroupInfo;
import pl.edu.pw.ii.bpmConsole.valueObjects.UserInfo;

import java.util.List;

public interface UserService {
    boolean validateCredentials(String username, String password);

    UserRights verifyRights(AuthUser user);

    List<UserInfo> listUsers();

    List<GroupInfo> listGroups();

    UserInfo getUser(String userId);

    GroupInfo getGroup(String groupId);

    void createUser(UserInfo userInfo);

    void editUser(UserInfo userInfo, String userId);

    void createGroup(GroupInfo groupInfo);

    void editGroup(GroupInfo groupInfo, String groupId);

    void deleteUser(String userId);

    void deleteGroup(String groupId);

    List<String> getUserGroups(String userId);

    void addMembership(String groupId, String userId);

    void deleteMembership(String groupId, String userId);
}
