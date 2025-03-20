package pl.lodz.p.port.infrastructure.rent;

import pl.lodz.p.core.domain.MongoUUID;

public interface RentRemove {
    void remove(MongoUUID uuid);
}
