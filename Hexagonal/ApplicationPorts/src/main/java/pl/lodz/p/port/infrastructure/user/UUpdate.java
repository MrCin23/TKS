package pl.lodz.p.port.infrastructure.user;

import pl.lodz.p.core.domain.MongoUUID;

import java.util.Map;

public interface UUpdate {
    void update(MongoUUID uuid, Map<String, Object> fieldsToUpdate);
    void update(MongoUUID uuid, String field, Object value);
}
