package pl.lodz.p.user.infrastructure.user;

import org.springframework.stereotype.Component;
import pl.lodz.p.user.core.domain.MongoUUID;

import java.util.Map;
@Component
public interface UUpdate {
    void update(MongoUUID uuid, Map<String, Object> fieldsToUpdate);
    void update(MongoUUID uuid, String field, Object value);
}
