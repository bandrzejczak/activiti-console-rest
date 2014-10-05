package pl.edu.pw.ii.ActivitiConsole.api;

import com.google.gson.Gson;
import org.restlet.*;
import org.restlet.data.*;
import org.springframework.stereotype.Component;
import pl.edu.pw.ii.ActivitiConsole.api.dataobjects.ValidateCredentialsDataIn;
import pl.edu.pw.ii.ActivitiConsole.api.dataobjects.ValidateCredentialsDataOut;

import javax.annotation.Resource;

import static org.junit.Assert.*;

@Component
public class MockRestlet {
    private static final String BASE_PATH = "/service";
    private static final String VALIDATE_CREDENTIALS_PATH = "/service/validateCredentials";

    private Gson gson = new Gson();
    private Request request;
    private Response response;
    private ChallengeResponse challengeResponse;
    @Resource(name="root")
    private Restlet rootRestlet;

    public MockRestlet login(String login, String password){
        ValidateCredentialsDataIn validateCredentialsDataIn = new ValidateCredentialsDataIn();
        validateCredentialsDataIn.setLogin(login);
        validateCredentialsDataIn.setPassword(password);
        post(VALIDATE_CREDENTIALS_PATH, validateCredentialsDataIn);
        ValidateCredentialsDataOut validateCredentialsDataOut = getEntity(ValidateCredentialsDataOut.class);
        if(!validateCredentialsDataOut.isValid())
            throw new InvalidCredentialsException();
        setChallengeResponse(login, password);
        return this;
    }

    private void setChallengeResponse(String login, String password) {
        challengeResponse = new ChallengeResponse(ChallengeScheme.HTTP_BASIC, login, password);
    }

    public MockRestlet logout(){
        challengeResponse = null;
        return this;
    }

    public MockRestlet get(String path){
        createRequest(Method.GET, path);
        perform();
        return this;
    }

    public MockRestlet post(String path, Object content){
        createRequest(Method.POST, path);
        String contentJSON = gson.toJson(content);
        request.setEntity(contentJSON, MediaType.APPLICATION_JSON);
        perform();
        return this;
    }

    private MockRestlet createRequest(Method method, String path){
        request = new Request(method, path);
        request.setRootRef(new Reference(BASE_PATH));
        request.setChallengeResponse(challengeResponse);
        return this;
    }

    private void perform(){
        response = new Response(request);
        rootRestlet.handle(request, response);
    }

    public MockRestlet expectStatusToBe(int statusCode){
        assertEquals(statusCode, response.getStatus().getCode());
        return this;
    }

    public MockRestlet expectToBeAuthenticated(){
        assertNotNull(challengeResponse);
        return this;
    }

    public MockRestlet expectNotToBeAuthenticated(){
        assertNull(challengeResponse);
        return this;
    }

    public <T> T getEntity(Class<T> clazz){
        return gson.fromJson(response.getEntityAsText(), clazz);
    }

}
