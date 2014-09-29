import org.activiti.engine.IdentityService;
import org.activiti.engine.identity.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"file:src/test/resources/application-context.xml",
        "file:src/test/resources/rest-servlet.xml"})
public class LoginTest {
    private @Autowired MockRestlet mockRestlet;
    private @Autowired IdentityService identityService;

    private static final String LOGIN = "test";
    private static final String VALID_PASSWORD = "test";
    private static final String INVALID_PASSWORD = "invalid";

    @Before
    public void setUp(){
        if(testUserDoesntExist())
            createTestUser();
    }

    private boolean testUserDoesntExist() {
        return identityService.createUserQuery().userId(LOGIN).singleResult() == null;
    }

    private void createTestUser() {
        User user = identityService.newUser(LOGIN);
        user.setPassword(VALID_PASSWORD);
        identityService.saveUser(user);
    }

    @Test(expected = InvalidCredentialsException.class)
    public void unsuccessfulLogin(){
        mockRestlet.logout()
                .login(LOGIN, INVALID_PASSWORD)
                .expectNotToBeAuthenticated();
        unauthorizedEntry();
    }

    @Test
    public void unauthorizedEntry(){
        mockRestlet.logout()
                .get("/service/test")
                .expectStatusToBe(401);
    }

    @Test
    public void successfulLogin(){
        mockRestlet.logout()
                .expectNotToBeAuthenticated()
                .login(LOGIN, VALID_PASSWORD)
                .expectToBeAuthenticated();
        authorizedEntry();
    }

    private void authorizedEntry() {
        mockRestlet.get("/service/test")
                .expectStatusToBe(200);
    }

}