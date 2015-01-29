package pl.edu.pw.ii.bpmConsole.rest.providers;

import io.dropwizard.jersey.errors.ErrorMessage;
import org.springframework.stereotype.Component;
import pl.edu.pw.ii.bpmConsole.interfaces.exceptions.ProcessEngineException;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Component
@Provider
public class ProcessEngineExceptionMapper implements ExceptionMapper<ProcessEngineException> {
    @Override
    public Response toResponse(ProcessEngineException exception) {
        return Response.status(exception.code)
                .entity(new ProcessEngineErrorMessage(exception))
                .type(MediaType.APPLICATION_JSON)
                .build();
    }

    class ProcessEngineErrorMessage extends ErrorMessage{
        public final String errorClass;
        public ProcessEngineErrorMessage(ProcessEngineException exception) {
            super(exception.code, exception.getMessage());
            errorClass = exception.getClass().getSimpleName();
        }
    }
}
