package pl.edu.pw.ii.bpmConsole.rest.resources.process;

import io.dropwizard.auth.Auth;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.edu.pw.ii.bpmConsole.interfaces.ProcessService;
import pl.edu.pw.ii.bpmConsole.rest.filters.BpmUser;
import pl.edu.pw.ii.bpmConsole.rest.resources.LinkBuilder;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Component
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Path("/processes")
public class ProcessResource {

    private final ProcessService processService;

    @Autowired
    public ProcessResource(ProcessService processService) {
        this.processService = processService;
    }


    @GET
    public Response list(@Auth BpmUser user) {
        return Response
                .ok(processService.listStartableProcesses(user.id, user.groups))
                .links(LinkBuilder.fromResource(ProcessResource.class).rel("self").build())
                .build();
    }

    @POST
    @Path("/{id}/start")
    public Response start(@Auth BpmUser user, @PathParam("id") String processDefinitionId) {
        processService.startProcess(processDefinitionId, user.id, user.groups);
        return Response
                .noContent()
                .links(LinkBuilder.fromResource(ProcessResource.class).rel("self").build())
                .build();
    }

}
