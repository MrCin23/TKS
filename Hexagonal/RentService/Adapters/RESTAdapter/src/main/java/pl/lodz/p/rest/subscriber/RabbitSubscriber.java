package pl.lodz.p.rest.subscriber;

import lombok.AllArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import pl.lodz.p.core.domain.user.Role;
import pl.lodz.p.core.services.service.ClientService;
import pl.lodz.p.user.rest.model.user.RESTClient;
import pl.lodz.p.user.rest.model.user.RESTRole;
import pl.lodz.p.user.rest.model.user.RESTUser;


@Component
@AllArgsConstructor
public class RabbitSubscriber {

    private final ClientService clientService;

    @RabbitListener(queues = RabbitConsts.CLIENT_CREATE_QUEUE_NAME)
    public void consumeCreate(@Payload RESTUser user,
                              @Header(AmqpHeaders.RECEIVED_ROUTING_KEY) String routingKey,
                              @Header(AmqpHeaders.RECEIVED_USER_ID) String userId) {
        try {
            System.out.println("=== OTRZYMANO WIADOMOŚĆ ===");
            System.out.println("Routing key: " + routingKey);
            System.out.println("User ID: " + userId);
            System.out.println("Odebrano użytkownika: " + user);

            if (user instanceof RESTClient) {
                RESTClient client = (RESTClient) user;
                clientService.createClient(new pl.lodz.p.core.domain.user.Client(
                        new pl.lodz.p.core.domain.MongoUUID(user.getEntityId().getUuid()),
                        client.getUsername(),
                        new pl.lodz.p.core.domain.user.Standard(),
                        client.getCurrentRents(),
                        client.isActive()
                ));
                System.out.println("Klient został utworzony pomyślnie");
            } else {
                System.err.println("Otrzymano nieprawidłowy typ użytkownika: " + user.getClass().getName());
            }
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