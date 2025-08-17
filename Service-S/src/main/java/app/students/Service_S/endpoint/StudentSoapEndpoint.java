package app.students.Service_S.endpoint;

import app.students.Service_S.service.StudentService;
import com.example.students.*;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import java.util.List;

@Endpoint
public class StudentSoapEndpoint {

    private static final String NAMESPACE_URI = "http://example.com/students";
    private final StudentService studentService;

    public StudentSoapEndpoint(StudentService studentService) {
        this.studentService = studentService;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getStudentRequest")
    @ResponsePayload
    public GetStudentResponse getStudent(@RequestPayload GetStudentRequest request) {
        GetStudentResponse resp = new GetStudentResponse();
        resp.setFirstName("HARDCODED");
        resp.setLastName("HARDCODED");
        resp.setFaculty("HARDCODED");
        resp.setRecordBook(request.getRecordBook());
        return resp;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getAllStudentsRequest")
    @ResponsePayload
    public GetAllStudentsResponse getAllStudents(@RequestPayload GetAllStudentsRequest request) {
        List<StudentData> students = studentService.getAllStudents();
        GetAllStudentsResponse resp = new GetAllStudentsResponse();
        resp.getStudent().addAll(students);
        return resp;
    }

}