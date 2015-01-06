package pl.edu.pw.ii.bpmConsole.rest.db;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import pl.edu.pw.ii.bpmConsole.interfaces.UserService;

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
        userService.createUser(user.login, user.password);
    }

    private void createUserGroup(User user) {
        userService.createGroup(user.login);
    }

    private void addUserToUserGroup(User user) {
        userService.createMembership(user.login, user.login);
    }
}
