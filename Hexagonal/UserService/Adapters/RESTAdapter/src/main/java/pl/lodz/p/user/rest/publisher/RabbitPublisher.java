package pl.lodz.p.user.rest.publisher;

import lombok.AllArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.stereotype.Component;
import pl.lodz.p.user.rest.model.user.RESTUser;

@Component
@AllArgsConstructor
public class RabbitPublisher {

    private final RabbitTemplate rabbitTemplate;

    public void sendCreate(RESTUser user) {
        try {
            System.out.println("Wysyłam użytkownika: " + user);

            rabbitTemplate.convertAndSend(
                    RabbitConsts.CLIENT_CREATE_EXCHANGE,
                    RabbitConsts.CLIENT_CREATE_KEY,
                    user,
                    message -> {
                        message.getMessageProperties().setContentType("application/json");
                        message.getMessageProperties().setHeader(
                                AmqpHeaders.RECEIVED_USER_ID,
                                user.getEntityId().getUuid().toString()
                        );
                        return message;
                    }
            );

            System.out.println("Użytkownik został wysłany pomyślnie: " + user);

        } catch (Exception e) {
            System.err.println("Błąd podczas wysyłania wiadomości: " + e.getMessage());
            e.printStackTrace();
        }
    }
}