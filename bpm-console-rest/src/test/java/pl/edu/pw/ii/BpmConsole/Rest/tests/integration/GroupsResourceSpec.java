package pl.edu.pw.ii.BpmConsole.Rest.tests.integration;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.GenericType;
import com.sun.jersey.api.client.WebResource;
import org.junit.Test;
import pl.edu.pw.ii.BpmConsole.Rest.db.User;

import java.util.List;

import static org.fest.assertions.api.Assertions.assertThat;

public class GroupsResourceSpec extends ActivitiIntegrationTest {

    @Test
    public void authorizedEntry() {
        //when
        WebResource resource = resource("/groups");
        List<String> groups = resource.get(new GenericType<>(List.class));
        //then
        assertThat(groups).contains(User.ADMIN.login);
    }

}
