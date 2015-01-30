package pl.edu.pw.ii.bpmConsole.rest.resources.user;

import io.dropwizard.auth.Auth;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.edu.pw.ii.bpmConsole.interfaces.UserService;
import pl.edu.pw.ii.bpmConsole.rest.filters.BpmUser;
import pl.edu.pw.ii.bpmConsole.rest.resources.LinkBuilder;
import pl.edu.pw.ii.bpmConsole.valueObjects.UserIdInfo;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Component
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Path("/groups/{groupId}/members")
public class GroupMembersResource {

    private final UserService userService;

    @Autowired
    public GroupMembersResource(UserService userService) {
        this.userService = userService;
    }

    @POST
    public Response add(@Auth BpmUser user, @PathParam("groupId") String groupId, @Valid UserIdInfo userIdInfo) {
        userService.addMembership(groupId, userIdInfo.id);
        return Response
                .ok()
                .links(LinkBuilder.fromResource(GroupResource.class).rel("self").build())
                .build();
    }
}
