package app.students.Service_S.listener;

import app.students.Service_S.configuration.RabbitmqConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class StudentRequestListener {
    private final Logger logger = LoggerFactory.getLogger(StudentRequestListener.class);

    public StudentRequestListener() {
        logger.info("StudentRequestListener: Rabbitmq configuration initialized");
    }

    @RabbitListener(queues = RabbitmqConfiguration.REQUEST_QUEUE)
    public String handleRequest(String message) {
        logger.info("handleRequest: Received request {}", message);
        return String.format("[Echo from Service S] %s", message);
    }
}
