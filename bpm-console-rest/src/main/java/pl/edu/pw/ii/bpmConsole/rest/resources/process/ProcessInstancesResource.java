package pl.edu.pw.ii.bpmConsole.rest.resources.process;

import io.dropwizard.auth.Auth;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.edu.pw.ii.bpmConsole.interfaces.ProcessService;
import pl.edu.pw.ii.bpmConsole.rest.resources.LinkBuilder;
import pl.edu.pw.ii.bpmConsole.rest.resources.RightsVerification;
import pl.edu.pw.ii.bpmConsole.valueObjects.AuthUser;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Component
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Path("/instances")
public class ProcessInstancesResource implements RightsVerification {
    private final ProcessService processService;

    @Autowired
    public ProcessInstancesResource(ProcessService processService) {
        this.processService = processService;
    }

    @GET
    public Response list(@Auth AuthUser user) {
        verifyAdminRights(user);
        return Response
                .ok(processService.listProcessInstances())
                .links(LinkBuilder.fromResource(ProcessInstancesResource.class).rel("self").build())
                .build();
    }
}
