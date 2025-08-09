package app.students.Service_R.controller;

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

    @GetMapping("/all")
    public String getAllStudents(@RequestBody String info) {
        log.info("Received {}", info);
        return info;
    }

    @GetMapping()
    public String test() {
        log.info("Received");
        return "Received";
    }
}
