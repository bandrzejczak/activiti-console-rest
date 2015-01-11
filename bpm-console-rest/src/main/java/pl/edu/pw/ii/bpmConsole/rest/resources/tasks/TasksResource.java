package pl.edu.pw.ii.bpmConsole.rest.resources.tasks;

import io.dropwizard.auth.Auth;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.edu.pw.ii.bpmConsole.interfaces.TaskService;
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
@Path("/tasks")
public class TasksResource {

    private final TaskService taskService;

    @Autowired
    public TasksResource(TaskService taskService) {
        this.taskService = taskService;
    }

    @GET
    @Path("/inbox")
    public Response inbox(@Auth BpmUser user) {
        return Response
                .ok(taskService.listAssignedTo(user.id))
                .links(LinkBuilder.fromResource(TasksResource.class).rel("self").build())
                .build();
    }

    @GET
    @Path("/awaiting")
    public Response awaiting(@Auth BpmUser user) {
        return Response
                .ok(taskService.listAvailableFor(user.id, user.groups))
                .links(LinkBuilder.fromResource(TasksResource.class).rel("self").build())
                .build();
    }
}
