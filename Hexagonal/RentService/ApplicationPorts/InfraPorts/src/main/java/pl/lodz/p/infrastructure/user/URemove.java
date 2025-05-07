package pl.lodz.p.infrastructure.user;

import org.springframework.stereotype.Component;
import pl.lodz.p.core.domain.user.Client;

import javax.crypto.Cipher;

@Component
public interface URemove {
    void remove(Client client);
}
