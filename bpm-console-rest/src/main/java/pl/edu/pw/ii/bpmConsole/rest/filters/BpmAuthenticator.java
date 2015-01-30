package pl.edu.pw.ii.bpmConsole.rest.filters;

import com.google.common.base.Optional;
import io.dropwizard.auth.AuthenticationException;
import io.dropwizard.auth.Authenticator;
import io.dropwizard.auth.basic.BasicCredentials;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.edu.pw.ii.bpmConsole.interfaces.UserService;
import pl.edu.pw.ii.bpmConsole.valueObjects.AuthUser;

import java.util.List;

@Component
public class BpmAuthenticator implements Authenticator<BasicCredentials, AuthUser> {

    private UserService userService;

    @Autowired
    public BpmAuthenticator(UserService userService) {
        this.userService = userService;
    }

    @Override
    public Optional<AuthUser> authenticate(BasicCredentials credentials) throws AuthenticationException {
        if (credentialsAreValid(credentials))
            return Optional.of(getUserData(credentials));
        return Optional.absent();
    }

    private boolean credentialsAreValid(BasicCredentials credentials) {
        return userService.validateCredentials(credentials.getUsername(), credentials.getPassword());
    }

    private AuthUser getUserData(BasicCredentials credentials) {
        return new AuthUser(credentials.getUsername(), getUserGroups(credentials.getUsername()));
    }

    private List<String> getUserGroups(String username) {
        return userService.getUserGroups(username);
    }
}
