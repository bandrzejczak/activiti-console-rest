package pl.edu.pw.ii.bpmConsole.rest.resources.tasks;

import io.dropwizard.auth.Auth;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import pl.edu.pw.ii.bpmConsole.interfaces.TaskService;
import pl.edu.pw.ii.bpmConsole.rest.resources.LinkBuilder;
import pl.edu.pw.ii.bpmConsole.valueObjects.AuthUser;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Map;

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
    public Response inbox(@Auth AuthUser user) {
        return Response
                .ok(taskService.listAssignedTo(user.id))
                .links(LinkBuilder.fromResource(TasksResource.class).rel("self").build())
                .build();
    }

    @GET
    @Path("/awaiting")
    public Response awaiting(@Auth AuthUser user) {
        return Response
                .ok(taskService.listAvailableFor(user))
                .links(LinkBuilder.fromResource(TasksResource.class).rel("self").build())
                .build();
    }

    @POST
    @Path("/{taskId}/claim")
    public Response claim(@Auth AuthUser user, @PathParam("taskId") String taskId) {
        taskService.claim(taskId, user);
        return Response
                .noContent()
                .links(LinkBuilder.fromResource(TasksResource.class).rel("self").build())
                .build();
    }

    @POST
    @Path("/{taskId}/unclaim")
    public Response unclaim(@Auth AuthUser user, @PathParam("taskId") String taskId) {
        taskService.unclaim(taskId, user);
        return Response
                .ok()
                .links(LinkBuilder.fromResource(TasksResource.class).rel("self").build())
                .build();
    }

    @POST
    @Path("/{taskId}/submit")
    public Response submit(@Auth AuthUser user, @PathParam("taskId") String taskId, @RequestBody Map<String, String> properties) {
        taskService.submit(taskId, user.id, properties);
        return Response
                .ok()
                .links(LinkBuilder.fromResource(TasksResource.class).rel("self").build())
                .build();
    }

    @GET
    @Path("/{taskId}/form")
    public Response form(@Auth AuthUser user, @PathParam("taskId") String taskId) {
        return Response
                .ok(taskService.findFormForTask(taskId, user))
                .links(LinkBuilder.fromResource(TasksResource.class).rel("self").build())
                .build();
    }
}
