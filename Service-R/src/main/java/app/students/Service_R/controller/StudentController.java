package app.students.Service_R.controller;

import app.students.Service_R.sender.StudentRequestSender;
import com.example.students.GetAllStudentsResponse;
import com.example.students.GetStudentRequest;
import com.example.students.GetStudentResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/students")
public class StudentController {
    private final Logger log = LoggerFactory.getLogger(StudentController.class);
    private final StudentRequestSender sender;

    public StudentController(StudentRequestSender sender) {
        this.sender = sender;
    }

    @PostMapping("/all")
    public GetAllStudentsResponse getAllStudents() {
        log.info("In StudentController.getAllStudents()");
        return sender.requestAllStudents();
    }

    @PostMapping("/find")
    public GetStudentResponse findStudentByRecordBook(@RequestBody GetStudentRequest request) {
        log.info("StudentController.findStudentByRecordBook({})", request);
        return sender.sendGetStudentRequest(request);
    }
}
