package pl.edu.pw.ii.ActivitiConsole.api.resources;

import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;
import pl.edu.pw.ii.ActivitiConsole.api.SpringResource;
import pl.edu.pw.ii.ActivitiConsole.api.dataobjects.ValidateCredentialsDataIn;

@SpringResource("/test")
public class TestResource extends ServerResource {

    @Get
    public ValidateCredentialsDataIn testFunc(){
        ValidateCredentialsDataIn validateCredentialsDataIn = new ValidateCredentialsDataIn();
        validateCredentialsDataIn.setLogin(getRequest().getClientInfo().getUser().getName());
        return validateCredentialsDataIn;
    }
}
