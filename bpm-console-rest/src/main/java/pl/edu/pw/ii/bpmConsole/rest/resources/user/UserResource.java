package pl.edu.pw.ii.bpmConsole.rest.resources.user;

import io.dropwizard.auth.Auth;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.edu.pw.ii.bpmConsole.interfaces.UserService;
import pl.edu.pw.ii.bpmConsole.rest.filters.BpmUser;
import pl.edu.pw.ii.bpmConsole.rest.resources.LinkBuilder;
import pl.edu.pw.ii.bpmConsole.valueObjects.UserInfo;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Component
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Path("/users")
public class UserResource {
    private final UserService userService;

    @Autowired
    public UserResource(UserService userService) {
        this.userService = userService;
    }

    @GET
    public Response list(@Auth BpmUser user) {
        return Response
                .ok(userService.listUsers())
                .links(LinkBuilder.fromResource(UserResource.class).rel("self").build())
                .build();
    }

    @GET
    @Path("/{id}")
    public Response get(@Auth BpmUser user, @PathParam("id") String userId) {
        return Response
                .ok(userService.getUser(userId))
                .links(LinkBuilder.fromResource(UserResource.class).rel("self").build())
                .build();
    }

    @POST
    public Response create(@Auth BpmUser user, @Valid UserInfo userInfo) {
        userService.createUser(userInfo);
        return Response
                .ok()
                .links(LinkBuilder.fromResource(UserResource.class).rel("self").build())
                .build();
    }

    @PUT
    @Path("/{id}")
    public Response edit(@Auth BpmUser user, @PathParam("id") String userId, @Valid UserInfo userInfo) {
        userService.editUser(userInfo, userId);
        return Response
                .ok()
                .links(LinkBuilder.fromResource(UserResource.class).rel("self").build())
                .build();
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@Auth BpmUser user, @PathParam("id") String userId) {
        userService.deleteUser(userId);
        return Response
                .ok()
                .links(LinkBuilder.fromResource(UserResource.class).rel("self").build())
                .build();
    }

    @GET
    @Path("/{id}/groups")
    public Response getUserGroups(@Auth BpmUser user, @PathParam("id") String userId) {
        return Response
                .ok(userService.getUserGroups(userId))
                .links(LinkBuilder.fromResource(GroupResource.class).rel("self").build())
                .build();
    }
}
