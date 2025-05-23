package pl.lodz.p.user.core.domain.user;

import lombok.NoArgsConstructor;
import pl.lodz.p.user.core.domain.MongoUUID;

import java.util.UUID;

@NoArgsConstructor
public class ResourceManager extends User{
    public ResourceManager(String firstName, String surname, String username, String password, String emailAddress) {
        super(new MongoUUID(UUID.randomUUID()), firstName, username, password, surname, emailAddress, Role.RESOURCE_MANAGER, true);
    }

    public ResourceManager(MongoUUID uuid, String firstName, String surname, String username, String password, String emailAddress) {
        super(uuid, firstName, username, password, surname, emailAddress, Role.RESOURCE_MANAGER, true);
    }

    @Override
    public String toString() {
        return super.toString() + "::ResourceManager{}";
    }
}
