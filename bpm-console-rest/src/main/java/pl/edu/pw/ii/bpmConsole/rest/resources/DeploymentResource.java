package pl.edu.pw.ii.bpmConsole.rest.resources;

import io.dropwizard.auth.Auth;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.edu.pw.ii.bpmConsole.interfaces.DeploymentService;
import pl.edu.pw.ii.bpmConsole.rest.filters.BpmUser;
import pl.edu.pw.ii.bpmConsole.valueObjects.File;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

@Component
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
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

    @GET
    public Response list(@Auth BpmUser user) {
        return Response.ok(deploymentService.list()).build();
    }

}
