package pl.lodz.p.port.infrastructure.rent;

import pl.lodz.p.core.domain.MongoUUID;

import java.time.LocalDateTime;

public interface RentEnd {
    void endRent(MongoUUID uuid, LocalDateTime endTime);
}
