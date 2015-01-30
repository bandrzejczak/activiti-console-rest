package pl.edu.pw.ii.bpmConsole.rest.resources.user;

import io.dropwizard.auth.Auth;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.edu.pw.ii.bpmConsole.interfaces.UserService;
import pl.edu.pw.ii.bpmConsole.rest.resources.LinkBuilder;
import pl.edu.pw.ii.bpmConsole.rest.resources.RightsVerification;
import pl.edu.pw.ii.bpmConsole.valueObjects.AuthUser;
import pl.edu.pw.ii.bpmConsole.valueObjects.UserIdInfo;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Component
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Path("/groups/{groupId}/members")
public class GroupMembersResource implements RightsVerification {

    private final UserService userService;

    @Autowired
    public GroupMembersResource(UserService userService) {
        this.userService = userService;
    }

    @POST
    public Response add(@Auth AuthUser user, @PathParam("groupId") String groupId, @Valid UserIdInfo userIdInfo) {
        verifyAdminRights(user);
        userService.addMembership(groupId, userIdInfo.id);
        return Response
                .ok()
                .links(LinkBuilder.fromResource(GroupsResource.class).rel("self").build())
                .build();
    }

    @DELETE
    public Response delete(@Auth AuthUser user, @PathParam("groupId") String groupId, @Valid UserIdInfo userIdInfo) {
        verifyAdminRights(user);
        userService.deleteMembership(groupId, userIdInfo.id);
        return Response
                .ok()
                .links(LinkBuilder.fromResource(GroupsResource.class).rel("self").build())
                .build();
    }
}
