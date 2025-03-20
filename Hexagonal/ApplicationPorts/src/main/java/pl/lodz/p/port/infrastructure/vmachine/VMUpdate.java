package pl.lodz.p.port.infrastructure.vmachine;

import pl.lodz.p.core.domain.MongoUUID;

import java.util.Map;

public interface VMUpdate {
    void update(MongoUUID uuid, Map<String, Object> fieldsToUpdate);
    void update(MongoUUID uuid, String field, Object value);
}
