package pl.edu.pw.ii.bpmConsole.rest.resources;

import io.dropwizard.auth.Auth;
import org.springframework.beans.factory.annotation.Autowired;
import pl.edu.pw.ii.bpmConsole.interfaces.DeploymentService;
import pl.edu.pw.ii.bpmConsole.rest.filters.BpmUser;
import pl.edu.pw.ii.bpmConsole.valueObjects.File;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

@JsonResource
@Path("/deployment")
public class DeploymentResource {

    DeploymentService deploymentService;

    @Autowired
    public DeploymentResource(DeploymentService deploymentService) {
        this.deploymentService = deploymentService;
    }

    @POST
    public Response deploy(@Auth BpmUser user, File file) {
        deploymentService.create(file).deploy();
        return Response.created(
                UriBuilder
                        .fromResource(DeploymentResource.class)
                        .build()
        ).build();
    }

}
