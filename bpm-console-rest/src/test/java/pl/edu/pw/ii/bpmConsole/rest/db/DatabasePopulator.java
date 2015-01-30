package pl.edu.pw.ii.bpmConsole.rest.db;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import pl.edu.pw.ii.bpmConsole.interfaces.UserService;
import pl.edu.pw.ii.bpmConsole.valueObjects.GroupInfo;
import pl.edu.pw.ii.bpmConsole.valueObjects.GroupType;

import java.util.Arrays;

@Component
public class DatabasePopulator implements ApplicationListener<ContextRefreshedEvent> {

    private UserService userService;
    private static boolean populated;

    @Autowired
    public DatabasePopulator(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void onApplicationEvent(final ContextRefreshedEvent event) {
        if (!populated) {
            Arrays
                .stream(User.values())
                .forEach(this::createUserAndGroup);
            populated = true;
        }
    }

    private void createUserAndGroup(User user) {
        createUser(user);
        createUserGroup(user);
        addUserToUserGroup(user);
    }

    private void createUser(User user) {
        userService.createUser(user.toUserInfo());
    }

    private void createUserGroup(User user) {
        GroupInfo groupInfo = new GroupInfo();
        groupInfo.id = user.login;
        groupInfo.type = GroupType.SECURITY_ROLE;
        userService.createGroup(groupInfo);
    }

    private void addUserToUserGroup(User user) {
        userService.createMembership(user.login, user.login);
    }
}
