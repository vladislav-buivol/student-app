package app.students.Service_S.configuration;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitmqConfiguration {
    public static final String EXCHANGE_NAME = "student.data.exchange";
    public static final String REQUEST_QUEUE = "student.request.queue";

    @Bean
    public TopicExchange topicExchange() {
        return new TopicExchange(EXCHANGE_NAME);
    }

    @Bean
    public Queue requestQueue() {
        return new Queue(REQUEST_QUEUE);
    }

    @Bean
    public Binding requestBinding(Queue requestQueue, TopicExchange exchange) {
        return BindingBuilder
                .bind(requestQueue)
                .to(exchange)
                .with("request.student.*");
    }
}
