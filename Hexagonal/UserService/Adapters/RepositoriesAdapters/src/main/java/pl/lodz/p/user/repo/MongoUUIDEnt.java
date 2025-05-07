package pl.lodz.p.user.repo;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Setter
@Getter
@NoArgsConstructor
@EqualsAndHashCode
public class MongoUUIDEnt {
    private UUID uuid;

    public MongoUUIDEnt(UUID uuid) {
        this.uuid = uuid;
    }

    @Override
    public String toString() {
        return uuid.toString();
    }
}
