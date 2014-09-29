package pl.edu.pw.ii.ActivitiConsole;

import org.activiti.engine.IdentityService;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;
import org.springframework.beans.factory.annotation.Autowired;
import pl.edu.pw.ii.ActivitiConsole.dataobjects.ValidateCredentialsDataIn;
import pl.edu.pw.ii.ActivitiConsole.dataobjects.ValidateCredentialsDataOut;

@SpringResource(value = "/validateCredentials", secured = false)
public class ValidateCredentialsResource extends ServerResource {

    @Autowired
    private IdentityService identityService;

    @Post
    public ValidateCredentialsDataOut validateCredentials(ValidateCredentialsDataIn credentials){
        boolean isPasswordCorrect = identityService
                .checkPassword(credentials.getLogin(), credentials.getPassword());
        ValidateCredentialsDataOut result = new ValidateCredentialsDataOut();
        result.setValid(isPasswordCorrect);
        return result;
    }
}
