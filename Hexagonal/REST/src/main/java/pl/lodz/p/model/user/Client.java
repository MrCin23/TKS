package pl.lodz.p.model.user;


import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonDiscriminator;
import org.bson.codecs.pojo.annotations.BsonProperty;
import pl.lodz.p.model.AbstractEntityMgd;
import pl.lodz.p.model.MongoUUID;
//import org.springframework.data.mongodb.core.mapping.Document;


import java.util.UUID;


@Setter
@Getter
@NoArgsConstructor
@BsonDiscriminator(value="Client", key="_clazz")
public class Client extends User{
    @BsonProperty("clientType")
    private ClientType clientType;
    @Min(0)
    @BsonProperty("currentRents")
    private int currentRents;

    @Override
    public String toString() {
        return super.toString() + "::Client{" +
                "clientType=" + clientType +
                ", currentRents=" + currentRents +
                '}';
    }

    public Client(String firstName,
                  String surname,
                  String username,
                  String password,
                  String emailAddress,
                  @BsonProperty("clientType") ClientType clientType) {
        super(new MongoUUID(UUID.randomUUID()), firstName, username, password, surname, emailAddress, Role.CLIENT, true);
        this.clientType = clientType;
        this.currentRents = 0;
    }

    public Client(MongoUUID userId,
                  String firstName,
                  String username,
                  String password,
                  String surname,
                  String emailAddress,
                  Role role,
                  boolean active,
                  @BsonProperty("clientType") ClientType clientType,
                  @BsonProperty("currentRents") int currentRents) {
        super(userId, firstName, username, password, surname, emailAddress, role, active);
        this.clientType = clientType;
        this.currentRents = currentRents;
    }
}
