package pl.lodz.p.infrastructure.user;

import org.springframework.stereotype.Component;
import pl.lodz.p.core.domain.user.Client;

@Component
public interface UAdd {
    void add(Client client);
}
