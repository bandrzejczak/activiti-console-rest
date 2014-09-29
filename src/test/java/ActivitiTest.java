import org.activiti.engine.IdentityService;
import org.activiti.engine.identity.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"file:src/test/resources/application-context.xml",
        "file:src/test/resources/rest-servlet.xml"})
public class ActivitiTest {

    @Autowired
    IdentityService identityService;

    @Test
    public void activitiTest(){
        User user = identityService.newUser("testUser");
        identityService.saveUser(user);
        List<User> users = identityService
                            .createUserQuery()
                            .userId("testUser")
                            .orderByUserId()
                            .asc()
                            .list();
        assertEquals(1, users.size());
    }
}
