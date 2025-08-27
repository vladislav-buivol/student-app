package app.students.Service_S.listener;

import app.students.Service_S.configuration.rabbitmq.RabbitmqConfiguration;
import com.example.students.GetAllStudentsRequest;
import com.example.students.GetAllStudentsResponse;
import com.example.students.GetStudentRequest;
import com.example.students.GetStudentResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.ws.client.core.WebServiceTemplate;

@Service
public class StudentRequestListener {
    private final Logger log = LoggerFactory.getLogger(StudentRequestListener.class);
    private final WebServiceTemplate wsTemplate;

    @Value("${service-s.wsEndpoint}")
    private String wsEndpoint;

    public StudentRequestListener(WebServiceTemplate wsTemplate) {
        this.wsTemplate = wsTemplate;
        log.info("StudentRequestListener: Rabbitmq configuration initialized");
    }

    @RabbitListener(queues = RabbitmqConfiguration.GET_ALL_QUEUE)
    public GetAllStudentsResponse handleGetAllRequest(GetAllStudentsRequest request) {
        log.info("StudentRequestListener: Rabbitmq configuration received");
        return (GetAllStudentsResponse) wsTemplate
                .marshalSendAndReceive(wsEndpoint, request);
    }

    @RabbitListener(queues = RabbitmqConfiguration.FIND_QUEUE)
    public GetStudentResponse handleFindRequest(GetStudentRequest request) {
        log.info("handleFindRequest:");
        return (GetStudentResponse) wsTemplate.marshalSendAndReceive(wsEndpoint, request);
    }
}
