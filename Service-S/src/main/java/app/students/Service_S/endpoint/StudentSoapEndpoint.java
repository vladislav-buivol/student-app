package app.students.Service_S.endpoint;

import com.example.students.GetStudentRequest;
import com.example.students.GetStudentResponse;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

@Endpoint
public class StudentSoapEndpoint {

    private static final String NAMESPACE_URI = "http://example.com/students";

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getStudentRequest")
    @ResponsePayload
    public GetStudentResponse getStudent(@RequestPayload GetStudentRequest request) {
        // хардкодим ответ, имитация БД
        GetStudentResponse response = new GetStudentResponse();
        response.setFirstName("Иван");
        response.setLastName("Иванов");
        response.setFaculty("ФКТИ");
        response.setRecordBook(request.getRecordBook());
        return response;

    }
}