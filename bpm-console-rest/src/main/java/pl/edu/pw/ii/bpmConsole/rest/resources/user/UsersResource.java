package pl.edu.pw.ii.bpmConsole.rest.resources.user;

import io.dropwizard.auth.Auth;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.edu.pw.ii.bpmConsole.interfaces.UserService;
import pl.edu.pw.ii.bpmConsole.rest.resources.LinkBuilder;
import pl.edu.pw.ii.bpmConsole.rest.resources.RightsVerification;
import pl.edu.pw.ii.bpmConsole.valueObjects.AuthUser;
import pl.edu.pw.ii.bpmConsole.valueObjects.AuthUserGroup;
import pl.edu.pw.ii.bpmConsole.valueObjects.UserInfo;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Component
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Path("/users")
public class UsersResource implements RightsVerification {
    private final UserService userService;

    @Autowired
    public UsersResource(UserService userService) {
        this.userService = userService;
    }

    @GET
    public Response list(@Auth AuthUser user) {
        verifyAdminRights(user);
        return Response
                .ok(userService.listUsers())
                .links(LinkBuilder.fromResource(UsersResource.class).rel("self").build())
                .build();
    }

    @GET
    @Path("/{id}")
    public Response get(@Auth AuthUser user, @PathParam("id") String userId) {
        userService.verifyRights(user).toUser(userId);
        return Response
                .ok(userService.getUser(userId))
                .links(LinkBuilder.fromResource(UsersResource.class).rel("self").build())
                .build();
    }

    @POST
    public Response create(@Auth AuthUser user, @Valid UserInfo userInfo) {
        verifyAdminRights(user);
        userService.createUser(userInfo, user.isMemberOf(AuthUserGroup.ADMIN));
        return Response
                .ok()
                .links(LinkBuilder.fromResource(UsersResource.class).rel("self").build())
                .build();
    }

    @PUT
    @Path("/{id}")
    public Response edit(@Auth AuthUser user, @PathParam("id") String userId, @Valid UserInfo userInfo) {
        userService.verifyRights(user).toUser(userId);
        userService.editUser(userInfo, userId, user.isMemberOf(AuthUserGroup.ADMIN));
        return Response
                .ok()
                .links(LinkBuilder.fromResource(UsersResource.class).rel("self").build())
                .build();
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@Auth AuthUser user, @PathParam("id") String userId) {
        verifyAdminRights(user);
        userService.deleteUser(userId);
        return Response
                .ok()
                .links(LinkBuilder.fromResource(UsersResource.class).rel("self").build())
                .build();
    }

    @GET
    @Path("/{id}/groups")
    public Response getUserGroups(@Auth AuthUser user, @PathParam("id") String userId) {
        userService.verifyRights(user).toUser(userId);
        return Response
                .ok(userService.getUserGroups(userId))
                .links(LinkBuilder.fromResource(UsersResource.class).rel("self").build())
                .build();
    }
}
