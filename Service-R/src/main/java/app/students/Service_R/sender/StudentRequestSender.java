package app.students.Service_R.sender;

import app.students.Service_R.configuration.RabbitmqConfiguration;
import app.students.Service_R.validator.GetStudentRequestValidator;
import com.example.students.GetAllStudentsRequest;
import com.example.students.GetAllStudentsResponse;
import com.example.students.GetStudentRequest;
import com.example.students.GetStudentResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class StudentRequestSender {
    private final Logger log = LoggerFactory.getLogger(StudentRequestSender.class);
    private final RabbitTemplate rabbitTemplate;
    private final GetStudentRequestValidator requestValidator;

    public StudentRequestSender(RabbitTemplate rabbitTemplate, GetStudentRequestValidator requestValidator) {
        this.rabbitTemplate = rabbitTemplate;
        this.requestValidator = requestValidator;
        log.info("StudentRequestSender: Initializing StudentRequestSender");
    }

    public GetAllStudentsResponse requestAllStudents() {
        log.info("StudentRequestSender: Executing sendGetAllRequest");
        return (GetAllStudentsResponse) rabbitTemplate.convertSendAndReceive(
                RabbitmqConfiguration.EXCHANGE_NAME,
                "request.student.getAll",
                new GetAllStudentsRequest()
        );
    }

    public GetStudentResponse sendGetStudentRequest(GetStudentRequest request) {
        requestValidator.validate(request);
        log.info("StudentRequestSender: Executing sendGetStudentRequest");
        return (GetStudentResponse) rabbitTemplate.convertSendAndReceive(
                RabbitmqConfiguration.EXCHANGE_NAME,
                "request.student.find",
                request
        );
    }
}
