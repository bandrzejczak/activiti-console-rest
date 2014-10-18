package pl.edu.pw.ii.ActivitiConsole.api;

import com.google.gson.Gson;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.Restlet;
import org.restlet.data.*;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

import static org.junit.Assert.assertEquals;

@Component
public class MockRestlet {
    private static final String BASE_PATH = "/service";

    private Gson gson = new Gson();
    private Request request;
    private Response response;
    private ChallengeResponse challengeResponse;
    @Resource(name="root")
    private Restlet rootRestlet;

    public MockRestlet login(String login, String password){
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

    public <T> T getEntity(Class<T> clazz){
        return gson.fromJson(response.getEntityAsText(), clazz);
    }

}
