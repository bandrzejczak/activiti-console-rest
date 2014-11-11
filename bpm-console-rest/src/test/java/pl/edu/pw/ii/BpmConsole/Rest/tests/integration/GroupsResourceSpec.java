package pl.edu.pw.ii.BpmConsole.Rest.tests.integration;

import org.junit.Test;
import pl.edu.pw.ii.BpmConsole.Rest.db.User;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Link;
import javax.ws.rs.core.Response;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class GroupsResourceSpec extends ActivitiIntegrationTest {

    @Test
    public void authorizedEntry() {
        //when
        WebTarget resource = target("/groups");
        Response response = resource.request().get();
        Link self = response.getLink("self");
        List<String> groups = response.readEntity(new GenericType<>(List.class));
        //then
        assertThat(self.getUri().getPath()).isEqualTo("/groups");
        assertThat(groups).contains(User.ADMIN.login);

    }

}
