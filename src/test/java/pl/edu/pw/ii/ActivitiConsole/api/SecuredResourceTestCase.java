package pl.edu.pw.ii.ActivitiConsole.api;

import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration({"file:src/test/resources/application-context.xml",
        "file:src/test/resources/rest-servlet.xml"})
public class SecuredResourceTestCase {

    @Autowired
    private MockRestlet mockRestlet;

    @Before
    public void setUp(){
        login();
    }

    private void login() {
        mockRestlet.login(User.ADMIN.login, User.ADMIN.password);
    }
}
