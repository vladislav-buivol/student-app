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
        return """
                <students>
                    <student>
                        <id>1</id>
                        <firstName>Ivan</firstName>
                        <lastName>Ivanov</lastName>
                        <faculty>CS</faculty>
                        <recordBook>12345</recordBook>
                    </student>
                    <student>
                        <id>2</id>
                        <firstName>Vasa</firstName>
                        <lastName>Ivanov</lastName>
                        <faculty>IKT</faculty>
                        <recordBook>67890</recordBook>
                    </student>
                </students>
                """;
    }
}
