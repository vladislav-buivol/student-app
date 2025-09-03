package app.students.Service_S.configuration.soap;

import com.example.students.ErrorInfo;
import com.example.students.GetStudentResponse;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.stereotype.Component;
import org.springframework.ws.context.MessageContext;
import org.springframework.ws.server.endpoint.AbstractEndpointExceptionResolver;

@Component
public class StudentExceptionResolver extends AbstractEndpointExceptionResolver {
    private final Jaxb2Marshaller marshaller;

    public StudentExceptionResolver(Jaxb2Marshaller marshaller) {
        this.marshaller = marshaller;
    }

    @Override
    protected boolean resolveExceptionInternal(MessageContext messageContext, Object endpoint, Exception ex) {
        try {
            GetStudentResponse response = createCustomResponseObject(ex);
            marshaller.marshal(response, messageContext.getResponse().getPayloadResult());
            return true;
        } catch (Exception e) {
            logger.error("Failed to marshal error response", e);
            return false;
        }
    }

    private GetStudentResponse createCustomResponseObject(Exception ex) {
        GetStudentResponse response = new GetStudentResponse();
        ErrorInfo error = new ErrorInfo();
        error.setCode("INTERNAL_ERROR");
        error.setMessage(ex.getMessage());
        response.setError(error);
        return response;
    }
}
