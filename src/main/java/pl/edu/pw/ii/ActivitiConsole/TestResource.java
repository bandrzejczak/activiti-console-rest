package pl.edu.pw.ii.ActivitiConsole;

import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;
import pl.edu.pw.ii.ActivitiConsole.dataobjects.ValidateCredentialsDataIn;

@SpringResource("/test")
public class TestResource extends ServerResource {

    @Get
    public ValidateCredentialsDataIn testFunc(){
        ValidateCredentialsDataIn validateCredentialsDataIn = new ValidateCredentialsDataIn();
        validateCredentialsDataIn.setLogin(getRequest().getClientInfo().getUser().getName());
        return validateCredentialsDataIn;
    }
}
