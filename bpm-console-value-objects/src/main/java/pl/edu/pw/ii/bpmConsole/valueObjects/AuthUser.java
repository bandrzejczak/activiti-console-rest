package pl.edu.pw.ii.bpmConsole.valueObjects;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created by bandrzejczak on 26.10.14.
 */
public class AuthUser {

    public String id;
    public List<String> groups = new ArrayList<>();

    public AuthUser(String id, List<String> groups) {
        this.id = id;
        this.groups = groups;
    }

    public Boolean isMemberOf(AuthUserGroup group) {
        return groups.contains(group.name);
    }

    /**
     * Use with Optional.orElseThrow like:
     * user.verifyMembership(group).orElseThrow(exception);
     */
    public Optional<Object> verifyMembership(AuthUserGroup group) {
        return groups.contains(group.name) ? Optional.of(new Object()) : Optional.empty();
    }
}
