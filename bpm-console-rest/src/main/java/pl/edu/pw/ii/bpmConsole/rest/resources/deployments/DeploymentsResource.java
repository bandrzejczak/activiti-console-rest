package pl.edu.pw.ii.bpmConsole.rest.resources.deployments;

import io.dropwizard.auth.Auth;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.edu.pw.ii.bpmConsole.interfaces.DeploymentService;
import pl.edu.pw.ii.bpmConsole.rest.filters.BpmUser;
import pl.edu.pw.ii.bpmConsole.rest.resources.LinkBuilder;
import pl.edu.pw.ii.bpmConsole.valueObjects.File;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

@Component
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Path("/deployments")
public class DeploymentsResource {

    DeploymentService deploymentService;

    @Autowired
    public DeploymentsResource(DeploymentService deploymentService) {
        this.deploymentService = deploymentService;
    }

    @POST
    public Response deploy(@Auth BpmUser user, File file) {
        deploymentService.create(file).deploy();
        return Response.created(
                UriBuilder
                        .fromResource(DeploymentsResource.class)
                        .build()
        )
                .links(LinkBuilder.fromResource(DeploymentsResource.class).rel("self").build())
                .build();
    }

    @GET
    public Response list(@Auth BpmUser user) {
        return Response
                .ok(deploymentService.list())
                .links(
                        LinkBuilder.fromResource(DeploymentsResource.class).rel("self").build(),
                        LinkBuilder.fromResource(DeploymentResource.class).rel("delete").build(":processDefinitionId")
                )
                .build();
    }


}
