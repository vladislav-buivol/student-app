package app.students.Service_R.listener;

import app.students.Service_R.configuration.RabbitmqConfiguration;
import com.example.students.GetStudentResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Unmarshaller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.io.StringReader;

@Service
public class StudentResponseListener {
    private final Logger logger = LoggerFactory.getLogger(StudentResponseListener.class);

    @RabbitListener(queues = RabbitmqConfiguration.RESPONSE_QUEUE)
    public void handleResponse(String xml) throws Exception {
        JAXBContext ctx = JAXBContext.newInstance(GetStudentResponse.class);
        Unmarshaller unmarshaller = ctx.createUnmarshaller();
        GetStudentResponse resp = (GetStudentResponse) unmarshaller.unmarshal(new StringReader(xml));

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(resp);

        logger.info("Final JSON: {}", json);
    }
}
