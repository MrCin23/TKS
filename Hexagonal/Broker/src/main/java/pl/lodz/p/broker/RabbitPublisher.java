package pl.lodz.p.broker;

import lombok.AllArgsConstructor;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.stereotype.Component;
import pl.lodz.p.broker.config.RabbitConsts;
import pl.lodz.p.broker.dto.UserDTO;

@Component
@AllArgsConstructor
public class RabbitPublisher {

    private final RabbitTemplate rabbitTemplate;

    public void sendCreate(UserDTO user) {
        MessageProperties messageProperties = new MessageProperties();
        messageProperties.setContentType("text/json");
        messageProperties.setHeader(AmqpHeaders.RECEIVED_USER_ID, user.getEntityId().getUuid().toString());
        Message message = rabbitTemplate.getMessageConverter().toMessage(user, messageProperties);

        rabbitTemplate.convertAndSend(RabbitConsts.CLIENT_CREATE_EXCHANGE, RabbitConsts.CLIENT_CREATE_RESPONSE_KEY, message);
    }
}
