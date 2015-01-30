package pl.edu.pw.ii.bpmConsole.rest.resources.process;

import io.dropwizard.auth.Auth;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.edu.pw.ii.bpmConsole.interfaces.ProcessService;
import pl.edu.pw.ii.bpmConsole.rest.filters.BpmUser;
import pl.edu.pw.ii.bpmConsole.rest.resources.LinkBuilder;

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
public class ProcessInstanceResource {
    private final ProcessService processService;

    @Autowired
    public ProcessInstanceResource(ProcessService processService) {
        this.processService = processService;
    }

    @GET
    public Response list(@Auth BpmUser user) {
        return Response
                .ok(processService.listProcessInstances())
                .links(LinkBuilder.fromResource(ProcessInstanceResource.class).rel("self").build())
                .build();
    }
}
