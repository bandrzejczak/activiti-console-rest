package pl.edu.pw.ii.bpmConsole.rest.tests.integration;

import org.eclipse.jetty.http.HttpStatus;
import org.junit.Test;
import pl.edu.pw.ii.bpmConsole.interfaces.exceptions.ZipFileHasNoProcessesException;
import pl.edu.pw.ii.bpmConsole.test.Base64Resource;
import pl.edu.pw.ii.bpmConsole.valueObjects.File;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class ExceptionMappersSpec extends BpmIntegrationTest {

    @Test
    public void shouldReturnNiceResponseWhenClassExtendsProcessEngineException() throws IOException {
        //given
        File singleProcess = new Base64Resource("testZip1.zip").toFile();
        //when
        Response response = target("/deployments")
                .request()
                .post(Entity.entity(singleProcess, MediaType.APPLICATION_JSON));
        //then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR_500);
        Map<String, String> map = response.readEntity(new GenericType<>(Map.class));
        assertThat(map.get("errorClass")).isEqualTo(ZipFileHasNoProcessesException.class.getSimpleName());
    }

}
