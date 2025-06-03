package pl.lodz.p.rest.subscriber;

import lombok.AllArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import pl.lodz.p.user.rest.model.user.RESTUser;

import java.util.UUID;

@Component
@AllArgsConstructor
public class RabbitPublisher {

    private final RabbitTemplate rabbitTemplate;

    public void sendCreate(HttpStatus status, String login) {
        try {
            System.out.println("Wysyłam status: " + status);

            rabbitTemplate.convertAndSend(
                    RabbitConsts.CLIENT_CREATE_RESPONSE_EXCHANGE,
                    RabbitConsts.CLIENT_CREATE_RESPONSE_KEY,
                    "",
                    message -> {
                        message.getMessageProperties().setContentType("application/json");
                        message.getMessageProperties().setHeader(
                                AmqpHeaders.RECEIVED_USER_ID,
                                login
                        );
                        message.getMessageProperties().setHeader(
                                AmqpHeaders.DELIVERY_TAG,
                                status.value()
                        );
                        return message;
                    }
            );

            System.out.println("Status został wysłany pomyślnie: " + status);

        } catch (Exception e) {
            System.err.println("Błąd podczas wysyłania wiadomości: " + e.getMessage());
            e.printStackTrace();
        }
    }
}