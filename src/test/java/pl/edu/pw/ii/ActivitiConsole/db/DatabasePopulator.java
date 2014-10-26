package pl.edu.pw.ii.ActivitiConsole.db;

import org.activiti.engine.IdentityService;
import org.activiti.engine.identity.Group;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class DatabasePopulator implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private IdentityService identityService;
    private static boolean populated;

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
        org.activiti.engine.identity.User activitiUser = identityService.newUser(user.login);
        activitiUser.setPassword(user.password);
        identityService.saveUser(activitiUser);
    }

    private void createUserGroup(User user) {
        Group group = identityService.newGroup(user.login);
        identityService.saveGroup(group);
    }

    private void addUserToUserGroup(User user) {
        identityService.createMembership(user.login, user.login);
    }
}
