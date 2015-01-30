package pl.edu.pw.ii.bpmConsole.rest.resources.process;

import io.dropwizard.auth.Auth;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.edu.pw.ii.bpmConsole.interfaces.ProcessService;
import pl.edu.pw.ii.bpmConsole.rest.resources.LinkBuilder;
import pl.edu.pw.ii.bpmConsole.valueObjects.AuthUser;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Component
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Path("/processes")
public class ProcessesResource {

    private final ProcessService processService;

    @Autowired
    public ProcessesResource(ProcessService processService) {
        this.processService = processService;
    }


    @GET
    public Response list(@Auth AuthUser user) {
        return Response
                .ok(processService.listStartableProcesses(user))
                .links(LinkBuilder.fromResource(ProcessesResource.class).rel("self").build())
                .build();
    }

    @POST
    @Path("/{id}/start")
    public Response start(@Auth AuthUser user, @PathParam("id") String processDefinitionId) {
        processService.startProcess(processDefinitionId, user);
        return Response
                .noContent()
                .links(LinkBuilder.fromResource(ProcessesResource.class).rel("self").build())
                .build();
    }

}
