package app.students.Service_R.listener;

import app.students.Service_R.configuration.RabbitmqConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class StudentResponseListener {
    private final Logger logger = LoggerFactory.getLogger(StudentResponseListener.class);

    @RabbitListener(queues = RabbitmqConfiguration.QUEUE_NAME)
    public void handleStudentResponse(String info) {
        logger.info("StudentResponseListener: Received {}", info);
    }
}
