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
        File singleProcess = new File();
        singleProcess.base64 = new Base64Resource("testZip1.zip").toString();
        singleProcess.fileName = "testZip1.zip";
        singleProcess.fileSize = (long) 1;
        //when
        Response response = target("/deployment")
                .request()
                .post(Entity.entity(singleProcess, MediaType.APPLICATION_JSON));
        //then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR_500);
        Map<String, String> map = response.readEntity(new GenericType<>(Map.class));
        assertThat(map.get("errorClass")).isEqualTo(ZipFileHasNoProcessesException.class.getSimpleName());
    }

    @Test
    public void shouldReturnNiceResponseWhenClassExtendsRuntimeException() throws IOException {
        //given
        File singleProcess = new File();
        singleProcess.base64 = new Base64Resource("empty.bpmn").toString();
        singleProcess.fileName = "empty.bpmn";
        singleProcess.fileSize = (long) 1;
        //when
        Response response = target("/deployment")
                .request()
                .post(Entity.entity(singleProcess, MediaType.APPLICATION_JSON));
        //then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR_500);
        Map<String, String> map = response.readEntity(new GenericType<>(Map.class));
        assertThat(map.get("message")).isEqualTo("Premature end of file.");
    }
}
