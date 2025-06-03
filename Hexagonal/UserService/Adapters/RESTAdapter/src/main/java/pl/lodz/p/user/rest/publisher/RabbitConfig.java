package pl.lodz.p.user.rest.publisher;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
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
        factory.setPort(5672);
        return factory;
    }

    @Bean
    public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
        RabbitAdmin admin = new RabbitAdmin(connectionFactory);
        admin.setAutoStartup(true);
        return admin;
    }

    @Bean
    public TopicExchange clientCreateExchange() {
        return new TopicExchange(
                RabbitConsts.CLIENT_CREATE_EXCHANGE,
                true,
                false
        );
    }

    @Bean
    public TopicExchange clientCreateResponseExchange() {
        return new TopicExchange(
                RabbitConsts.CLIENT_CREATE_RESPONSE_EXCHANGE,
                true,
                false
        );
    }

    @Bean
    public Queue clientCreateQueue() {
        return QueueBuilder.durable(RabbitConsts.CLIENT_CREATE_QUEUE_NAME)
                .withArgument("x-queue-type", "classic")
                .withArgument("x-max-length", 10000)
                .build();
    }

    @Bean
    public Queue clientCreateResponseQueue() {
        return QueueBuilder.durable(RabbitConsts.CLIENT_CREATE_RESPONSE_QUEUE_NAME)
                .build();
    }

    @Bean
    public Binding clientCreateBinding() {
        return BindingBuilder
                .bind(clientCreateQueue())
                .to(clientCreateExchange())
                .with(RabbitConsts.CLIENT_CREATE_KEY);
    }

    @Bean
    public Binding clientCreateResponseBinding() {
        return BindingBuilder
                .bind(clientCreateResponseQueue())
                .to(clientCreateResponseExchange())
                .with(RabbitConsts.CLIENT_CREATE_RESPONSE_KEY);
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(messageConverter());
        template.setMandatory(true);
        return template;
    }

    @Bean
    public MessageConverter messageConverter() {
        Jackson2JsonMessageConverter converter = new Jackson2JsonMessageConverter();
        converter.setCreateMessageIds(true);
        return converter;
    }

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(
            ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(messageConverter());
        factory.setMissingQueuesFatal(false);
        factory.setFailedDeclarationRetryInterval(5000L);
        factory.setConcurrentConsumers(1);
        factory.setMaxConcurrentConsumers(1);
        return factory;
    }
}