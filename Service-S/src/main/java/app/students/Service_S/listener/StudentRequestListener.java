package app.students.Service_S.listener;

import app.students.Service_S.configuration.RabbitmqConfiguration;
import com.example.students.GetAllStudentsRequest;
import com.example.students.GetAllStudentsResponse;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.ws.client.core.WebServiceTemplate;

import java.io.StringWriter;

@Service
public class StudentRequestListener {
    private final Logger logger = LoggerFactory.getLogger(StudentRequestListener.class);
    private final WebServiceTemplate wsTemplate;

    @Value("${service-s.wsEndpoint}")
    private String wsEndpoint;

    public StudentRequestListener(WebServiceTemplate wsTemplate) {
        this.wsTemplate = wsTemplate;
        logger.info("StudentRequestListener: Rabbitmq configuration initialized");
    }

    @RabbitListener(queues = RabbitmqConfiguration.REQUEST_QUEUE)
    public String handleGetAllRequest(String payload) throws JAXBException {
        logger.info("handleGetAllRequest:");
        // SOAP request to its own endpoint
        GetAllStudentsRequest getAllStudentsRequest = new GetAllStudentsRequest();
        GetAllStudentsResponse resp = (GetAllStudentsResponse) wsTemplate
                .marshalSendAndReceive(wsEndpoint, getAllStudentsRequest);
        return convertToxml(resp);
    }

    /**
     * Convert to xml XML
     */
    private String convertToxml(GetAllStudentsResponse resp) throws JAXBException {
        JAXBContext ctx = JAXBContext.newInstance(GetAllStudentsResponse.class);
        StringWriter writer = new StringWriter();
        ctx.createMarshaller().marshal(resp, writer);

        return writer.toString();
    }
}
