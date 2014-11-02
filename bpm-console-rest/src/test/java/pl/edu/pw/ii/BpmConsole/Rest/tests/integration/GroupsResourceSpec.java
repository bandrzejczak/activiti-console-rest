package pl.edu.pw.ii.BpmConsole.Rest.tests.integration;

import com.sun.jersey.api.client.GenericType;
import org.junit.Test;
import pl.edu.pw.ii.BpmConsole.Rest.db.User;

import java.util.List;

import static org.fest.assertions.api.Assertions.assertThat;

public class GroupsResourceSpec extends ActivitiIntegrationTest {

    @Test
    public void authorizedEntry() {
        //when
        List<String> groups = resource("/groups").get(new GenericType<>(List.class));
        //then
        assertThat(groups).contains(User.ADMIN.login);
    }

}
