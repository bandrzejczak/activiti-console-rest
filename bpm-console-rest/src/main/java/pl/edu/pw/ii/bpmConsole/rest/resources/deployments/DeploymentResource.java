package pl.edu.pw.ii.bpmConsole.rest.resources.deployments;

import io.dropwizard.auth.Auth;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.edu.pw.ii.bpmConsole.interfaces.DeploymentService;
import pl.edu.pw.ii.bpmConsole.rest.resources.LinkBuilder;
import pl.edu.pw.ii.bpmConsole.rest.resources.RightsVerification;
import pl.edu.pw.ii.bpmConsole.valueObjects.AuthUser;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Component
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Path("/deployments/{processDefinitionId}")
public class DeploymentResource implements RightsVerification {

    DeploymentService deploymentService;

    @Autowired
    public DeploymentResource(DeploymentService deploymentService) {
        this.deploymentService = deploymentService;
    }

    @DELETE
    public Response delete(@Auth AuthUser user, @PathParam("processDefinitionId") String processDefinitionId) {
        verifyAdminRights(user);
        deploymentService.delete(processDefinitionId);
        return Response
                .noContent()
                .links(LinkBuilder.fromResource(DeploymentsResource.class).rel("deployments").build())
                .build();
    }
}
