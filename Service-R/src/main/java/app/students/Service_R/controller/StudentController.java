package app.students.Service_R.controller;

import app.students.Service_R.sender.StudentRequestSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
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

    @GetMapping("/all")
    public String getAllStudents(@RequestBody String info) {
        String response = sender.sendGetAllRequest();
        log.info("StudentController.getAllStudents: RequestBody {}", info);
        log.info("RequestBody {}", info);
        return response;
    }

    @GetMapping()
    public String test() {
        log.info("StudentController.test: Received");
        return "Received";
    }
}
