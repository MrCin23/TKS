package pl.lodz.p.broker.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Setter
@Getter
@NoArgsConstructor
@EqualsAndHashCode
public class RESTMongoUUID {
    private UUID uuid;

    public RESTMongoUUID(UUID uuid) {
        this.uuid = uuid;
    }

    @Override
    public String toString() {
        return "RESTMongoUUID{" +
                "uuid=" + uuid +
                '}';
    }
}
