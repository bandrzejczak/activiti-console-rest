import org.activiti.engine.IdentityService;
import org.activiti.engine.identity.User;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@ContextConfiguration({"file:src/test/resources/application-context.xml",
        "file:src/test/resources/rest-servlet.xml"})
public class SecuredResourceTestCase {

    private static final String LOGIN = "test";
    private static final String PASSWORD = "test";

    @Autowired
    private MockRestlet mockRestlet;
    @Autowired
    private IdentityService identityService;

    @Before
    public void setUp(){
        if(testUserDoesntExist())
            createTestUser();
        login();
    }

    private boolean testUserDoesntExist() {
        return identityService.createUserQuery().userId(LOGIN).singleResult() == null;
    }

    private void createTestUser() {
        User user = identityService.newUser(LOGIN);
        user.setPassword(PASSWORD);
        identityService.saveUser(user);
    }

    private void login() {
        mockRestlet.login(LOGIN, PASSWORD);
    }
}
