package pl.edu.pw.ii.bpmConsole.rest.resources.tasks;

import io.dropwizard.auth.Auth;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.edu.pw.ii.bpmConsole.interfaces.TaskService;
import pl.edu.pw.ii.bpmConsole.interfaces.UserService;
import pl.edu.pw.ii.bpmConsole.rest.filters.BpmUser;
import pl.edu.pw.ii.bpmConsole.rest.resources.LinkBuilder;
import pl.edu.pw.ii.bpmConsole.valueObjects.Rights;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Component
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Path("/tasks")
public class TasksResource {

    private final TaskService taskService;
    private final UserService userService;

    @Autowired
    public TasksResource(TaskService taskService, UserService userService) {
        this.taskService = taskService;
        this.userService = userService;
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

    @POST
    @Path("/{taskId}/claim")
    public Response claim(@Auth BpmUser user, @PathParam("taskId") String taskId) {
        taskService.claim(taskId, user.id, user.groups);
        return Response
                .noContent()
                .links(LinkBuilder.fromResource(TasksResource.class).rel("self").build())
                .build();
    }

    @GET
    @Path("/{taskId}/form")
    public Response form(@Auth BpmUser user, @PathParam("taskId") String taskId) {
        Rights rightsToTask = userService.verifyRights(user.id, user.groups).toTask(taskId);
        return Response
                .ok(taskService.findFormForTask(taskId, rightsToTask))
                .links(LinkBuilder.fromResource(TasksResource.class).rel("self").build())
                .build();
    }
}
