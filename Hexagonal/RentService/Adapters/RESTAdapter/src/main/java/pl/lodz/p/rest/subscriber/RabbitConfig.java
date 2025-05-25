package pl.lodz.p.rest.subscriber;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;


@EnableRabbit
@Configuration
public class RabbitConfig {

    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory factory = new CachingConnectionFactory("localhost");
        factory.setUsername("admin");
        factory.setPassword("adminpassword");
        factory.setVirtualHost("/");
        return factory;
    }

    @Bean
    public Declarables declarables() {
        System.out.println("### Inicjalizacja RabbitMQ ###");

        TopicExchange exchange = new TopicExchange(
                RabbitConsts.CLIENT_CREATE_EXCHANGE,
                true,  // durable
                false  // autoDelete
        );

        Queue mainQueue = new Queue(
                RabbitConsts.CLIENT_CREATE_QUEUE_NAME,
                true,   // durable
                false,  // exclusive
                false,  // autoDelete
                Map.of(
                        "x-queue-type", "classic",
                        "x-max-length", 10000
                )
        );

        Queue responseQueue = new Queue(
                RabbitConsts.CLIENT_CREATE_RESPONSE_QUEUE_NAME,
                true, false, false
        );

        return new Declarables(
                exchange,
                mainQueue,
                responseQueue,
                BindingBuilder.bind(mainQueue).to(exchange).with(RabbitConsts.CLIENT_CREATE_KEY),
                BindingBuilder.bind(responseQueue).to(exchange).with(RabbitConsts.CLIENT_CREATE_RESPONSE_KEY)
        );
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(messageConverter());
        return template;
    }

    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(
            ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(messageConverter());
        factory.setMissingQueuesFatal(false);
        factory.setFailedDeclarationRetryInterval(5000L);
        return factory;
    }
}