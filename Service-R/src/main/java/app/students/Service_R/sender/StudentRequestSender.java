package app.students.Service_R.sender;

import app.students.Service_R.configuration.RabbitmqConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class StudentRequestSender {
    private final Logger logger = LoggerFactory.getLogger(StudentRequestSender.class);
    private final RabbitTemplate rabbitTemplate;

    public StudentRequestSender(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
        logger.info("StudentRequestSender: Initializing StudentRequestSender");
    }

    public String sendGetAllRequest() {
        logger.info("StudentRequestSender: Executing sendGetAllRequest");
        Object response = rabbitTemplate.convertSendAndReceive(
                RabbitmqConfiguration.EXCHANGE_NAME,
                "request.student.getAll",
                "Message-sendGetAllRequest"
        );
        return response != null ? response.toString() : "No response";
    }
}
