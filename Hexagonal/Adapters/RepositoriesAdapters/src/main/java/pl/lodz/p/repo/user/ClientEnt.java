package pl.lodz.p.repo.user;


import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.codecs.pojo.annotations.BsonDiscriminator;
import org.bson.codecs.pojo.annotations.BsonProperty;
import pl.lodz.p.repo.MongoUUIDEnt;

import java.util.UUID;


@Setter
@Getter
@NoArgsConstructor
@BsonDiscriminator(value="Client", key="_clazz")
public class ClientEnt extends UserEnt {
    @BsonProperty("clientType")
    private ClientTypeEnt clientTypeEnt;
    @Min(0)
    @BsonProperty("currentRents")
    private int currentRents;

    @Override
    public String toString() {
        return super.toString() + "::Client{" +
                "clientType=" + clientTypeEnt +
                ", currentRents=" + currentRents +
                '}';
    }

    public ClientEnt(String firstName,
                     String surname,
                     String username,
                     String password,
                     String emailAddress,
                     @BsonProperty("clientType") ClientTypeEnt clientTypeEnt) {
        super(new MongoUUIDEnt(UUID.randomUUID()), firstName, username, password, surname, emailAddress, RoleEnt.CLIENT, true);
        this.clientTypeEnt = clientTypeEnt;
        this.currentRents = 0;
    }

    public ClientEnt(MongoUUIDEnt userId,
                     String firstName,
                     String username,
                     String password,
                     String surname,
                     String emailAddress,
                     RoleEnt roleEnt,
                     boolean active,
                     @BsonProperty("clientType") ClientTypeEnt clientTypeEnt,
                     @BsonProperty("currentRents") int currentRents) {
        super(userId, firstName, username, password, surname, emailAddress, roleEnt, active);
        this.clientTypeEnt = clientTypeEnt;
        this.currentRents = currentRents;
    }
}
