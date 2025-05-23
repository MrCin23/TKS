package pl.lodz.p.broker.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class RabbitConfig {

    @Bean(RabbitConsts.CLIENT_CREATE_QUEUE_NAME)
    public Queue clientCreateQueue() {
        return new Queue(RabbitConsts.CLIENT_CREATE_QUEUE_NAME, true);
    }

    @Bean(RabbitConsts.CLIENT_CREATE_RESPONSE_QUEUE_NAME)
    public Queue clientCreateResponseQueue() {
        return new Queue(RabbitConsts.CLIENT_CREATE_RESPONSE_QUEUE_NAME, true);
    }

    @Bean(RabbitConsts.TOPIC_EXCHANGE_BEAN_NAME)
    public TopicExchange topicExchange() {
        return new TopicExchange(RabbitConsts.CLIENT_CREATE_EXCHANGE);
    }

    @Bean
    public Binding bindingClientCreate(@Qualifier(RabbitConsts.CLIENT_CREATE_QUEUE_NAME) Queue queue, @Qualifier(RabbitConsts.TOPIC_EXCHANGE_BEAN_NAME) TopicExchange topicExchange) {
        return BindingBuilder.bind(queue).to(topicExchange).with(RabbitConsts.CLIENT_CREATE_KEY);
    }

    @Bean
    public Binding bindingClientCreateResponse(@Qualifier(RabbitConsts.CLIENT_CREATE_RESPONSE_QUEUE_NAME) Queue queue, @Qualifier(RabbitConsts.TOPIC_EXCHANGE_BEAN_NAME) TopicExchange topicExchange) {
        return BindingBuilder.bind(queue).to(topicExchange).with(RabbitConsts.CLIENT_CREATE_RESPONSE_KEY);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

}
