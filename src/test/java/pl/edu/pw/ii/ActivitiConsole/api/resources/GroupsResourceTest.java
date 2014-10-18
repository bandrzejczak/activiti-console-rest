package pl.edu.pw.ii.ActivitiConsole.api.resources;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import pl.edu.pw.ii.ActivitiConsole.api.MockRestlet;
import pl.edu.pw.ii.ActivitiConsole.api.SecuredResourceTestCase;

import java.util.List;

import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"file:src/test/resources/application-context.xml",
        "file:src/test/resources/rest-servlet.xml"})
public class GroupsResourceTest extends SecuredResourceTestCase {
    public static final String GROUPS_RESOURCE = "/service/groups";
    public static final String LOGGED_USER_GROUP = "admin";
    @Autowired
    private MockRestlet mockRestlet;


    @Test
    public void shouldGetUserGroups() {
        //when
        mockRestlet.get(GROUPS_RESOURCE);

        //then
        mockRestlet.expectStatusToBe(200);
        assertTrue(mockRestlet.getEntity(List.class).contains(LOGGED_USER_GROUP));
    }

}
