package pl.lodz.p.soap.model.user;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.lodz.p.soap.model.SOAPMongoUUID;

import java.util.UUID;


@Setter
@Getter
@NoArgsConstructor
public class SOAPClient extends SOAPUser {
    private SOAPClientType clientType;
    private int currentRents;

    @Override
    public String toString() {
        return super.toString() + "::Client{" +
                "clientType=" + clientType +
                ", currentRents=" + currentRents +
                '}';
    }

    public SOAPClient(String firstName,
                      String surname,
                      String username,
                      String password,
                      String emailAddress,
                      SOAPClientType clientType) {
        super(new SOAPMongoUUID(UUID.randomUUID()), firstName, username, password, surname, emailAddress, SOAPRole.CLIENT, true);
        this.clientType = clientType;
        this.currentRents = 0;
    }

    public SOAPClient(SOAPMongoUUID userId,
                      String firstName,
                      String username,
                      String password,
                      String surname,
                      String emailAddress,
                      SOAPRole SOAPRole,
                      boolean active,
                      SOAPClientType SOAPClientType,
                      int currentRents) {
        super(userId, firstName, username, password, surname, emailAddress, SOAPRole, active);
        this.clientType = SOAPClientType;
        this.currentRents = currentRents;
    }
}
