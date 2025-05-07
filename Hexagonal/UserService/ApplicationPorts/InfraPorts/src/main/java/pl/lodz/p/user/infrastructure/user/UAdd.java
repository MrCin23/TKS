package pl.lodz.p.user.infrastructure.user;

import org.springframework.stereotype.Component;
import pl.lodz.p.user.core.domain.user.User;
@Component
public interface UAdd {
    void add(User user);
}
