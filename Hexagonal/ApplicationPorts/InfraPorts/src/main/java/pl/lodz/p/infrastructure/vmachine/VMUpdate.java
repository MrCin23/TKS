package pl.lodz.p.infrastructure.vmachine;

import org.springframework.stereotype.Component;
import pl.lodz.p.core.domain.MongoUUID;

import java.util.Map;
@Component
public interface VMUpdate {
    void update(MongoUUID uuid, Map<String, Object> fieldsToUpdate);
    void update(MongoUUID uuid, String field, Object value);
}
