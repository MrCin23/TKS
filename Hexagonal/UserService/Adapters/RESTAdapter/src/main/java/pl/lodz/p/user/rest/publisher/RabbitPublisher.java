package pl.lodz.p.user.rest.publisher;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.stereotype.Component;
import pl.lodz.p.user.rest.model.user.RESTUser;

@Component
@AllArgsConstructor
public class RabbitPublisher {

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final RabbitTemplate rabbitTemplate;

    public void sendCreate(RESTUser user) throws JsonProcessingException {
        rabbitTemplate.convertAndSend(
                RabbitConsts.CLIENT_CREATE_EXCHANGE,
                RabbitConsts.CLIENT_CREATE_KEY,
                user,  // nie serializuj ręcznie
                message -> {
                    message.getMessageProperties().setContentType("application/json");
                    message.getMessageProperties().setHeader(
                            AmqpHeaders.RECEIVED_USER_ID,
                            user.getEntityId().getUuid().toString()
                    );
                    return message;
                }
        );
        System.out.println("New user sent: " + user);
    }

}
