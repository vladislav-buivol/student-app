package app.students.Service_S.endpoint;

import app.students.Service_S.configuration.exception.StudentNotFoundException;
import app.students.Service_S.service.StudentService;
import com.example.students.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import java.util.List;

@Endpoint
public class StudentSoapEndpoint {

    private static final String NAMESPACE_URI = "http://example.com/students";
    private final StudentService studentService;
    private final Logger log = LoggerFactory.getLogger(StudentSoapEndpoint.class);

    public StudentSoapEndpoint(StudentService studentService) {
        this.studentService = studentService;
    }

   /* @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getStudentRequest")
    @ResponsePayload
    public GetStudentResponse getStudent(@RequestPayload GetStudentRequest request) {
        if (request == null || request.getRecordBook() == null) {
            //throw new InvalidStudentRequestException("recordBook is required");
        }
        log.info("SOAP getStudent called with recordBook={}", request.getRecordBook());
        StudentData studentData = studentService.getStudentByRecordBook(request.getRecordBook());
        if (studentData == null) {
            //throw new StudentNotFoundException("Student not found for recordBook=" + request.getRecordBook());
        }
        GetStudentResponse resp = new GetStudentResponse();
        if (studentData != null) {
            resp.setFirstName(studentData.getFirstName());
            resp.setLastName(studentData.getLastName());
            resp.setFaculty(studentData.getFaculty());
            resp.setRecordBook(studentData.getRecordBook());
            resp.setCreatedAt(studentData.getCreatedAt());
            resp.setProfileImageUrl(studentData.getProfileImageUrl());
        }
        log.info("SOAP getStudent returning dataFound={}", studentData != null);
        return resp;
    }*/

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getStudentRequest")
    @ResponsePayload
    public GetStudentResponse getStudent(@RequestPayload GetStudentRequest request) {
        log.info("SOAP getStudent called with recordBook={}", request.getRecordBook());
        StudentData studentData = studentService.getStudentByRecordBook(request.getRecordBook());
        if (studentData == null) {
            throw new StudentNotFoundException("Student not found for recordBook=" + request.getRecordBook());
        }
        GetStudentResponse resp = new GetStudentResponse();
        resp.setStudent(studentData);
        return resp;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getAllStudentsRequest")
    @ResponsePayload
    public GetAllStudentsResponse getAllStudents(@RequestPayload GetAllStudentsRequest request) {
        log.info("SOAP getAllStudents called");
        List<StudentData> students = studentService.getAllStudents();
        GetAllStudentsResponse resp = new GetAllStudentsResponse();
        resp.getStudent().addAll(students);
        return resp;
    }
}