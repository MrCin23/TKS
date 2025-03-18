package pl.lodz.p.repo.user;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Getter;
import lombok.Setter;
import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonProperty;
import pl.lodz.p.repo.AbstractEnt;
import pl.lodz.p.repo.MongoUUIDEnt;

@Getter
@Setter
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "_clazz"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = StandardEnt.class, name = "standard"),
        @JsonSubTypes.Type(value = PremiumEnt.class, name = "admin")
})
public abstract class ClientTypeEnt extends AbstractEnt {

    @BsonProperty("maxRentedMachines")
    protected int maxRentedMachines;

    @BsonProperty("name")
    protected String name;

    @BsonCreator
    public ClientTypeEnt(@BsonProperty("_id") MongoUUIDEnt uuid,
                         @BsonProperty("maxRentedMachines") int maxRentedMachines,
                         @BsonProperty("name") String name){
        super(uuid);
        this.maxRentedMachines = maxRentedMachines;
        this.name = name;
    }

    public String toString() {
        return "Class: " + this.getClass().getSimpleName() + " " + this.getMaxRentedMachines() + " " + this.getName();
    }
}
