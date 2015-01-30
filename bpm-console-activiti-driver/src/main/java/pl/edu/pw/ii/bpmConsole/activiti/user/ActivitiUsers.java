package pl.edu.pw.ii.bpmConsole.activiti.user;

import org.activiti.engine.IdentityService;
import org.activiti.engine.identity.User;
import pl.edu.pw.ii.bpmConsole.valueObjects.UserInfo;

import java.util.List;
import java.util.stream.Collectors;

public class ActivitiUsers {
    private final IdentityService identityService;

    public ActivitiUsers(IdentityService identityService) {
        this.identityService = identityService;
    }

    public List<UserInfo> list() {
        return identityService
                .createUserQuery()
                .list()
                .stream()
                .map(this::mapUser)
                .collect(Collectors.toList());
    }

    private UserInfo mapUser(User user) {
        UserInfo userInfo = new UserInfo();
        userInfo.id = user.getId();
        userInfo.firstName = user.getFirstName();
        userInfo.lastName = user.getLastName();
        userInfo.email = user.getEmail();
        return userInfo;
    }
}
