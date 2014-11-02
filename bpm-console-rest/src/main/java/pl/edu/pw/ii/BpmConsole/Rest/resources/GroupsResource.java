package pl.edu.pw.ii.BpmConsole.Rest.resources;

import com.codahale.metrics.annotation.Timed;
import io.dropwizard.auth.Auth;
import org.springframework.stereotype.Component;
import pl.edu.pw.ii.BpmConsole.Rest.BpmUser;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Component
@Path("/groups")
@Produces(MediaType.APPLICATION_JSON)
public class GroupsResource {

    @GET
    @Timed
    public List<String> getUserGroups(@Auth BpmUser user) {
        return user.groups;
    }

}
