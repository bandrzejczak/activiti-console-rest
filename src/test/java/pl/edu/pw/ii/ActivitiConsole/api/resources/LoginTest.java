package pl.edu.pw.ii.ActivitiConsole.api.resources;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import pl.edu.pw.ii.ActivitiConsole.api.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"file:src/test/resources/application-context.xml",
        "file:src/test/resources/rest-servlet.xml"})
public class LoginTest {
    @Autowired
    private MockRestlet mockRestlet;

    private static final String INVALID_PASSWORD = "invalid";


    @Test
    public void unsuccessfulLogin(){
        mockRestlet
                .logout()
                .login(User.ADMIN.login, INVALID_PASSWORD)
                .get("/service/test")
                .expectStatusToBe(401);
    }

    @Test
    public void unauthorizedEntry(){
        mockRestlet
                .logout()
                .get("/service/test")
                .expectStatusToBe(401);
    }

    @Test
    public void successfulLogin(){
        //when
        mockRestlet
                .login(User.ADMIN.login, User.ADMIN.password);

        //then
        expectToBeAuthorized();
    }

    private void expectToBeAuthorized() {
        mockRestlet.get("/service/test")
                .expectStatusToBe(200);
    }

}