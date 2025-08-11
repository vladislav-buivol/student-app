package app.students.Service_R.configuration;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitmqConfiguration {
    public static final String EXCHANGE_NAME = "student.data.exchange";
    public static final String RESPONSE_QUEUE = "student.response.queue";

    @Bean
    public TopicExchange topicExchange() {
        return new TopicExchange(EXCHANGE_NAME);
    }

    @Bean
    public Queue responseQueue() {
        return new Queue(RESPONSE_QUEUE);
    }

    @Bean
    public Binding responseBinding(Queue responseQueue, TopicExchange exchange) {
        return BindingBuilder
                .bind(responseQueue)
                .to(exchange)
                .with("response.student.*");
    }


}
