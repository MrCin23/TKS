package pl.lodz.p.user.rest.publisher;

import lombok.AllArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import pl.lodz.p.user.core.domain.user.Role;
import pl.lodz.p.user.core.services.service.UserService;
import pl.lodz.p.user.rest.model.user.RESTClient;
import pl.lodz.p.user.rest.model.user.RESTRole;
import pl.lodz.p.user.rest.model.user.RESTUser;

import java.util.UUID;


@Component
@AllArgsConstructor
public class RabbitSubscriber {

    private final UserService userService;

    @RabbitListener(queues = RabbitConsts.CLIENT_CREATE_RESPONSE_QUEUE_NAME)
    public void consumeCreate(@Header(AmqpHeaders.DELIVERY_TAG) Long status,
                              @Header(AmqpHeaders.RECEIVED_ROUTING_KEY) String routingKey,
                              @Header(AmqpHeaders.RECEIVED_USER_ID) String login) {
        try {
            System.out.println("=== OTRZYMANO WIADOMOŚĆ ===");
            System.out.println("Routing key: " + routingKey);
            System.out.println("User ID: " + login);
            System.out.println("Odebrano status: " + status);
            if(status != HttpStatus.CREATED.value()) {
                userService.deleteUser(login);
            }
//            System.out.println("Klient został utworzony pomyślnie");
        } catch (Exception e) {
            System.err.println("Błąd podczas przetwarzania wiadomości: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private Role convert(RESTRole ent) {
        if (ent == RESTRole.ADMIN) {
            return Role.ADMIN;
        }
        else if (ent == RESTRole.CLIENT) {
            return Role.CLIENT;
        }
        else if (ent == RESTRole.RESOURCE_MANAGER) {
            return Role.RESOURCE_MANAGER;
        }
        return null;
    }
}