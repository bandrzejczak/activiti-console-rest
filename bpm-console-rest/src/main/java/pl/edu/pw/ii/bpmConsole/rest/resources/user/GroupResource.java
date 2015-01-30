package pl.edu.pw.ii.bpmConsole.rest.resources.user;

import io.dropwizard.auth.Auth;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.edu.pw.ii.bpmConsole.interfaces.UserService;
import pl.edu.pw.ii.bpmConsole.rest.filters.BpmUser;
import pl.edu.pw.ii.bpmConsole.rest.resources.LinkBuilder;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Component
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Path("/groups")
public class GroupResource {
    private final UserService userService;

    @Autowired
    public GroupResource(UserService userService) {
        this.userService = userService;
    }

    @GET
    public Response list(@Auth BpmUser user) {
        return Response
                .ok(userService.listGroups())
                .links(LinkBuilder.fromResource(GroupResource.class).rel("self").build())
                .build();
    }

    @GET
    @Path("/{id}")
    public Response get(@Auth BpmUser user, @PathParam("id") String groupId) {
        return Response
                .ok(userService.getGroup(groupId))
                .links(LinkBuilder.fromResource(GroupResource.class).rel("self").build())
                .build();
    }

    @GET
    @Path("/user")
    public Response getUserGroups(@Auth BpmUser user) {
        return Response
                .ok(userService.getUserGroups(user.id))
                .links(LinkBuilder.fromResource(GroupResource.class).rel("self").build())
                .build();
    }
}
