package pl.edu.pw.ii.bpmConsole.rest.resources;

import pl.edu.pw.ii.bpmConsole.interfaces.exceptions.AccessDeniedException;
import pl.edu.pw.ii.bpmConsole.valueObjects.AuthUser;
import pl.edu.pw.ii.bpmConsole.valueObjects.AuthUserGroup;

/**
 * Why is it an interface?
 * <p>
 * - Maybe some day I'll want my resources to have some base class
 * - I like Scala's trait idea, so this is how it can be realized in Java
 */
public interface RightsVerification {
    default void verifyAdminRights(AuthUser user) {
        user.verifyMembership(AuthUserGroup.ADMIN).orElseThrow(AccessDeniedException::new);
    }
}
