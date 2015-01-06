package pl.edu.pw.ii.bpmConsole.rest.resources;

import com.codahale.metrics.annotation.Timed;
import io.dropwizard.auth.Auth;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.edu.pw.ii.bpmConsole.interfaces.UserService;
import pl.edu.pw.ii.bpmConsole.rest.filters.BpmUser;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Component
@Path("/groups")
@Produces(MediaType.APPLICATION_JSON)
public class GroupsResource {

    UserService userService;

    @Autowired
    public GroupsResource(UserService userService) {
        this.userService = userService;
    }

    @GET
    @Timed
    public Response getUserGroups(@Auth BpmUser user) {
        return Response
                .ok(userService.getUserGroups(user.id))
                .links(LinkBuilder.fromResource(GroupsResource.class).rel("self").build())
                .build();
    }

}
