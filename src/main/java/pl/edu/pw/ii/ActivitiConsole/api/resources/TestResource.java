package pl.edu.pw.ii.ActivitiConsole.api.resources;

import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;
import pl.edu.pw.ii.ActivitiConsole.api.SpringResource;

@SpringResource("/test")
public class TestResource extends ServerResource {

    @Get
    public String testFunc(){
        return getRequest().getClientInfo().getUser().getName();
    }
}
