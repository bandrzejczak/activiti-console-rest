package pl.edu.pw.ii.bpmConsole.rest.db;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import pl.edu.pw.ii.bpmConsole.interfaces.ProcessEngine;

import java.util.Arrays;

@Component
public class DatabasePopulator implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private ProcessEngine processEngine;
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
        processEngine.createUser(user.login, user.password);
    }

    private void createUserGroup(User user) {
        processEngine.createGroup(user.login);
    }

    private void addUserToUserGroup(User user) {
        processEngine.createMembership(user.login, user.login);
    }
}
