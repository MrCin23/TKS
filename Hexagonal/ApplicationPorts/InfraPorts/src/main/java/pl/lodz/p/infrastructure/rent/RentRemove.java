package pl.lodz.p.infrastructure.rent;

import org.springframework.stereotype.Component;
import pl.lodz.p.core.domain.MongoUUID;
@Component
public interface RentRemove {
    void remove(MongoUUID uuid);
}
