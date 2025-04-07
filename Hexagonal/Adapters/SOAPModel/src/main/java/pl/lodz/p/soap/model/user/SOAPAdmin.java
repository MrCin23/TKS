package pl.lodz.p.soap.model.user;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.NoArgsConstructor;
import pl.lodz.p.soap.model.SOAPMongoUUID;

import java.util.UUID;

@NoArgsConstructor
public class SOAPAdmin extends SOAPUser {
    public SOAPAdmin(String firstName, String surname, String username, String password, String emailAddress) {
        super(new SOAPMongoUUID(UUID.randomUUID()), firstName, username, password, surname, emailAddress, SOAPRole.ADMIN, true);
    }

    public SOAPAdmin(SOAPMongoUUID uuid, String firstName, String surname, String username, String emailAddress, String password) {
        super(uuid, firstName, username, password, surname, emailAddress, SOAPRole.ADMIN, true);
    }

    @Override
    public String toString() {
        return super.toString() + "::Admin{}";
    }
}
