package pl.edu.pw.ii.bpmConsole.rest.providers;

import io.dropwizard.jersey.errors.ErrorMessage;
import org.eclipse.jetty.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Component
@Provider
public class IllegalArgumentExceptionMapper implements ExceptionMapper<IllegalArgumentException> {
    @Override
    public Response toResponse(IllegalArgumentException exception) {
        return Response.status(HttpStatus.BAD_REQUEST_400)
                .entity(new ErrorMessage(HttpStatus.BAD_REQUEST_400, exception.getMessage()))
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}
