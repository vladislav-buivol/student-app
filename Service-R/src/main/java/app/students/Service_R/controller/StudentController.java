package app.students.Service_R.controller;

import app.students.Service_R.sender.StudentRequestSender;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
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
    private final XmlMapper xmlMapper;
    private final ObjectMapper objectMapper;

    public StudentController(StudentRequestSender sender) {
        this.sender = sender;
        this.xmlMapper = new XmlMapper();
        this.objectMapper = new ObjectMapper();
    }


    @GetMapping("/all")
    public String getAllStudents(@RequestBody String info) throws Exception {
        String xmlResponse = sender.sendGetAllRequest();
        log.info("StudentController.getAllStudents: RequestBody {}", info);
        log.info("RequestBody {}", info);
        log.info("Response {}", xmlResponse);
        JsonNode jsonNode = xmlMapper.readTree(xmlResponse.getBytes());

        return objectMapper.writeValueAsString(jsonNode);
    }

    @GetMapping()
    public String test() {
        log.info("StudentController.test: Received");
        return "Received";
    }
}
