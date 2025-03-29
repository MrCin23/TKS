package pl.lodz.p.soap.model.user;

import lombok.NoArgsConstructor;
import pl.lodz.p.soap.model.SOAPMongoUUID;

import java.util.UUID;

@NoArgsConstructor
public class SOAPResourceManager extends SOAPUser {
    public SOAPResourceManager(String firstName, String surname, String username, String password, String emailAddress) {
        super(new SOAPMongoUUID(UUID.randomUUID()), firstName, username, password, surname, emailAddress, SOAPRole.RESOURCE_MANAGER, true);
    }

    public SOAPResourceManager(SOAPMongoUUID uuid, String firstName, String surname, String username, String password, String emailAddress) {
        super(uuid, firstName, username, password, surname, emailAddress, SOAPRole.RESOURCE_MANAGER, true);
    }

    @Override
    public String toString() {
        return super.toString() + "::ResourceManager{}";
    }
}
