package pl.edu.pw.ii.bpmConsole.activiti.user;

import org.activiti.engine.IdentityService;
import org.activiti.engine.identity.User;
import pl.edu.pw.ii.bpmConsole.interfaces.exceptions.NoSuchUserException;
import pl.edu.pw.ii.bpmConsole.valueObjects.UserInfo;

import java.util.List;
import java.util.Optional;
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

    public UserInfo get(String userId) {
        User user = findUser(userId).orElseThrow(() -> new NoSuchUserException(userId));
        return mapUser(user);
    }

    private Optional<User> findUser(String userId) {
        return Optional.ofNullable(
                identityService
                        .createUserQuery()
                        .userId(userId)
                        .singleResult()
        );
    }
}
