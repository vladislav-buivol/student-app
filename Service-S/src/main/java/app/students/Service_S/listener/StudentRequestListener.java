package app.students.Service_S.listener;

import app.students.Service_S.configuration.RabbitmqConfiguration;
import com.example.students.GetStudentRequest;
import com.example.students.GetStudentResponse;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import org.springframework.ws.client.core.WebServiceTemplate;

import java.io.StringWriter;

@Service
public class StudentRequestListener {
    private final Logger logger = LoggerFactory.getLogger(StudentRequestListener.class);
    private final WebServiceTemplate wsTemplate;

    public StudentRequestListener(WebServiceTemplate wsTemplate) {
        this.wsTemplate = wsTemplate;
        logger.info("StudentRequestListener: Rabbitmq configuration initialized");
    }

    @RabbitListener(queues = RabbitmqConfiguration.REQUEST_QUEUE)
    public String handleRequest(String message) throws JAXBException {
        logger.info("handleRequest: Received request {}", message);

        // Создаём SOAP-запрос
        GetStudentRequest req = new GetStudentRequest();
        req.setRecordBook("12345"); // пример данных

        // Отправляем SOAP-запрос и получаем SOAP-ответ
        GetStudentResponse resp = (GetStudentResponse)
                wsTemplate.marshalSendAndReceive("http://localhost:8081/ws", req);

        // Превращаем ответ в XML для RabbitMQ
        JAXBContext ctx = JAXBContext.newInstance(GetStudentResponse.class);
        StringWriter writer = new StringWriter();
        ctx.createMarshaller().marshal(resp, writer);

        return writer.toString();
    }
}
