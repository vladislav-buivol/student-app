package app.students.Service_S.configuration;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2XmlMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitmqConfiguration {
    public static final String EXCHANGE_NAME = "student.data.exchange";
    public static final String GET_ALL_QUEUE = "student.request.getAll.queue";
    public static final String FIND_QUEUE = "student.request.find.queue";

    @Bean
    public TopicExchange topicExchange() {
        return new TopicExchange(EXCHANGE_NAME);
    }

    @Bean
    public Queue getAllQueue() {
        return new Queue(GET_ALL_QUEUE);
    }

    @Bean
    public Queue findQueue() {
        return new Queue(FIND_QUEUE);
    }

    @Bean
    public Binding requestBindingGetAll(Queue getAllQueue, TopicExchange exchange) {
        return BindingBuilder
                .bind(getAllQueue)
                .to(exchange)
                .with("request.student.getAll");
    }

    @Bean
    public Binding requestBindingFind(Queue findQueue, TopicExchange exchange) {
        return BindingBuilder
                .bind(findQueue)
                .to(exchange)
                .with("request.student.find");
    }

    @Bean
    public MessageConverter xmlMessageConverter() {
        return new Jackson2XmlMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(xmlMessageConverter());
        return template;
    }
}
