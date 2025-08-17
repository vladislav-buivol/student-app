package app.students.Service_R.controller;

import app.students.Service_R.sender.StudentRequestSender;
import com.example.students.GetAllStudentsResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.StringReader;

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

    @PostMapping("/all")
    public String getAllStudents() throws IOException, JAXBException {
        //TODO logic move to service
        log.info("StudentController.getAllStudents()");
        String soapResponse = sender.sendGetAllRequest();
        log.info("StudentController.test: Received {}", soapResponse);
        JAXBContext ctx = JAXBContext.newInstance(GetAllStudentsResponse.class);
        Unmarshaller unmarshaller = ctx.createUnmarshaller();
        GetAllStudentsResponse response = (GetAllStudentsResponse) unmarshaller.unmarshal(new StringReader(soapResponse));
        // Convert Java object to JSON
        try {
            return objectMapper.writeValueAsString(response);
        } catch (Exception e) {
            throw new RuntimeException("Failed to convert response to JSON", e);
        }
    }
}
