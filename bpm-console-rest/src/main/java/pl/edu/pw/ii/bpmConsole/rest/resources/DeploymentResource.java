package pl.edu.pw.ii.bpmConsole.rest.resources;

import io.dropwizard.auth.Auth;
import org.springframework.beans.factory.annotation.Autowired;
import pl.edu.pw.ii.bpmConsole.interfaces.ProcessEngine;
import pl.edu.pw.ii.bpmConsole.rest.filters.BpmUser;
import pl.edu.pw.ii.bpmConsole.valueObjects.File;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

@JsonResource
@Path("/deployment")
public class DeploymentResource {

    @Autowired
    ProcessEngine processEngine;

    @POST
    public Response deploy(@Auth BpmUser user, File file) {
        processEngine.createDeployment(file).deploy();
        return Response.created(
                UriBuilder
                        .fromResource(DeploymentResource.class)
                        .build()
        ).build();
    }

}
