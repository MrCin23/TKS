package pl.lodz.p.infrastructure.rent;

import org.springframework.stereotype.Component;
import pl.lodz.p.core.domain.MongoUUID;

import java.time.LocalDateTime;
@Component
public interface RentEnd {
    void endRent(MongoUUID uuid, LocalDateTime endTime);
}
