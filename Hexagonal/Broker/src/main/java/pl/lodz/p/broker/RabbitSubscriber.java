package pl.lodz.p.broker;

import lombok.AllArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import pl.lodz.p.broker.config.RabbitConsts;
import pl.lodz.p.core.services.service.ClientService;
import pl.lodz.p.user.core.domain.MongoUUID;
import pl.lodz.p.user.core.domain.user.Client;
import pl.lodz.p.user.core.domain.user.Role;
import pl.lodz.p.user.core.domain.user.Standard;
import pl.lodz.p.user.core.services.service.UserService;
import pl.lodz.p.user.rest.model.user.RESTClient;
import pl.lodz.p.user.rest.model.user.RESTRole;
import pl.lodz.p.user.rest.model.user.RESTUser;



@Component
@AllArgsConstructor
public class RabbitSubscriber {

    private final UserService userService;
    private final ClientService clientService;

    @RabbitListener(queues = RabbitConsts.CLIENT_CREATE_QUEUE_NAME)
    public void consumeCreate(@Payload RESTUser user,
                              @Header(AmqpHeaders.RECEIVED_ROUTING_KEY) String routingKey,
                              @Header(AmqpHeaders.RECEIVED_USER_ID) String userId) {
        Client client = new Client(
                new MongoUUID(user.getEntityId().getUuid()),
                user.getFirstName(),
                user.getUsername(),
                user.getPassword(),
                user.getSurname(),
                user.getEmailAddress(),
                convert(user.getRole()),
                user.isActive(),
                new Standard(),
                ((RESTClient)user).getCurrentRents());
        userService.createUser(client);
        clientService.createClient(new pl.lodz.p.core.domain.user.Client(
                new pl.lodz.p.core.domain.MongoUUID(user.getEntityId().getUuid()),
                user.getUsername(),
                new pl.lodz.p.core.domain.user.Standard(),
                ((RESTClient)user).getCurrentRents(),
                user.isActive()
        ));
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
