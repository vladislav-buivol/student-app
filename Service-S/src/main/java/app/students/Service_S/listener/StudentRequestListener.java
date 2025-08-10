package app.students.Service_S.listener;

import app.students.Service_S.configuration.RabbitmqConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class StudentRequestListener {
    private final RabbitTemplate rabbitTemplate;
    private final Logger logger = LoggerFactory.getLogger(StudentRequestListener.class);

    public StudentRequestListener(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
        logger.info("StudentRequestListener: Rabbitmq configuration initialized");
    }

    @RabbitListener(queues = RabbitmqConfiguration.REQUEST_QUEUE)
    public void handleRequest(String message) {
        logger.info("handleRequest: Received request {}", message);
        String routingKey = "student.request.queue.unknown";
        if (message.contains("GetAll")) {
            logger.info("StudentRequestListener: received request.student.getAll");
            routingKey = "response.student.getAll";
        } else {
            logger.info("StudentRequestListener: handleRequest: Received UNKNOWN request: {} ", message);
        }

        rabbitTemplate.convertAndSend(
                RabbitmqConfiguration.EXCHANGE_NAME,
                routingKey,
                String.format("[Echo from Service S] %s", message)
        );
    }
}
