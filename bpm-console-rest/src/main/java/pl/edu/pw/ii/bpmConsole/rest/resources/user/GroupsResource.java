package pl.edu.pw.ii.bpmConsole.rest.resources.user;

import io.dropwizard.auth.Auth;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.edu.pw.ii.bpmConsole.interfaces.UserService;
import pl.edu.pw.ii.bpmConsole.rest.resources.LinkBuilder;
import pl.edu.pw.ii.bpmConsole.rest.resources.RightsVerification;
import pl.edu.pw.ii.bpmConsole.valueObjects.AuthUser;
import pl.edu.pw.ii.bpmConsole.valueObjects.GroupInfo;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Component
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Path("/groups")
public class GroupsResource implements RightsVerification {
    private final UserService userService;

    @Autowired
    public GroupsResource(UserService userService) {
        this.userService = userService;
    }

    @GET
    public Response list(@Auth AuthUser user) {
        verifyAdminRights(user);
        return Response
                .ok(userService.listGroups())
                .links(LinkBuilder.fromResource(GroupsResource.class).rel("self").build())
                .build();
    }

    @GET
    @Path("/{id}")
    public Response get(@Auth AuthUser user, @PathParam("id") String groupId) {
        verifyAdminRights(user);
        return Response
                .ok(userService.getGroup(groupId))
                .links(LinkBuilder.fromResource(GroupsResource.class).rel("self").build())
                .build();
    }

    @POST
    public Response create(@Auth AuthUser user, @Valid GroupInfo groupInfo) {
        verifyAdminRights(user);
        userService.createGroup(groupInfo);
        return Response
                .ok()
                .links(LinkBuilder.fromResource(GroupsResource.class).rel("self").build())
                .build();
    }

    @PUT
    @Path("/{id}")
    public Response edit(@Auth AuthUser user, @PathParam("id") String groupId, @Valid GroupInfo groupInfo) {
        verifyAdminRights(user);
        userService.editGroup(groupInfo, groupId);
        return Response
                .ok()
                .links(LinkBuilder.fromResource(GroupsResource.class).rel("self").build())
                .build();
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@Auth AuthUser user, @PathParam("id") String groupId) {
        verifyAdminRights(user);
        userService.deleteGroup(groupId);
        return Response
                .ok()
                .links(LinkBuilder.fromResource(GroupsResource.class).rel("self").build())
                .build();
    }
}
