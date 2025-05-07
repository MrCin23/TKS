package pl.lodz.p.repo.client.data;


import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.codecs.pojo.annotations.BsonDiscriminator;
import org.bson.codecs.pojo.annotations.BsonProperty;
import pl.lodz.p.repo.AbstractEnt;
import pl.lodz.p.repo.MongoUUIDEnt;

import java.util.UUID;


@Setter
@Getter
@NoArgsConstructor
@BsonDiscriminator(value="Client", key="_clazz")
public class ClientEnt extends AbstractEnt {
    @NotBlank(message = "Username cannot be blank")
    @Size(min = 4, max = 20)
    @BsonProperty("username")
    private String username;
    @BsonProperty("active")
    private boolean active;
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

    public ClientEnt(@BsonProperty("username") String username,
                     @BsonProperty("clientType") ClientTypeEnt clientTypeEnt,
                     @BsonProperty("active") boolean active) {
        this.username = username;
        this.clientTypeEnt = clientTypeEnt;
        this.active = active;
        this.currentRents = 0;
    }

    public ClientEnt(@BsonProperty("_id") MongoUUIDEnt userId,
            @BsonProperty("username") String username,
                     @BsonProperty("active") boolean active,
                     @BsonProperty("clientType") ClientTypeEnt clientTypeEnt,
                     @BsonProperty("currentRents") int currentRents) {
        super(userId);
        this.username = username;
        this.active = active;
        this.clientTypeEnt = clientTypeEnt;
        this.currentRents = currentRents;
    }
}
