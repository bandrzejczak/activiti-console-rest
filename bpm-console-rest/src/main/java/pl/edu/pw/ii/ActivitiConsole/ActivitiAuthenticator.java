package pl.edu.pw.ii.ActivitiConsole;

import com.google.common.base.Optional;
import io.dropwizard.auth.AuthenticationException;
import io.dropwizard.auth.Authenticator;
import io.dropwizard.auth.basic.BasicCredentials;
import org.activiti.engine.IdentityService;
import org.activiti.engine.identity.Group;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ActivitiAuthenticator implements Authenticator<BasicCredentials, ActivitiUser> {

    private IdentityService identityService;

    @Autowired
    public ActivitiAuthenticator(IdentityService identityService){
        this.identityService = identityService;
    }

    @Override
    public Optional<ActivitiUser> authenticate(BasicCredentials credentials) throws AuthenticationException {
        if(credentialsAreValid(credentials))
            return Optional.of(getUserData(credentials));
        return Optional.absent();
    }

    private boolean credentialsAreValid(BasicCredentials credentials) {
        return identityService.checkPassword(credentials.getUsername(), credentials.getPassword());
    }

    private ActivitiUser getUserData(BasicCredentials credentials) {
        return new ActivitiUser(credentials.getUsername(), getUserGroups(credentials.getUsername()));
    }

    private List<String> getUserGroups(String username) {
        return identityService
                .createGroupQuery()
                .groupMember(username)
                .list()
                .stream()
                .map(Group::getId)
                .collect(Collectors.toList());
    }
}
