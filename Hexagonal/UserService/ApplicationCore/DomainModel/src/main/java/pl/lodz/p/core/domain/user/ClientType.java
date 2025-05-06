package pl.lodz.p.core.domain.user;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Getter;
import lombok.Setter;
import pl.lodz.p.core.domain.AbstractEntityMgd;
import pl.lodz.p.core.domain.MongoUUID;

@Getter
@Setter
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "_clazz"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = Standard.class, name = "standard"),
        @JsonSubTypes.Type(value = Premium.class, name = "admin")
})
public abstract class ClientType extends AbstractEntityMgd {

    protected int maxRentedMachines;

    protected String name;

    public ClientType(MongoUUID uuid,
                      int maxRentedMachines,
                      String name){
        super(uuid);
        this.maxRentedMachines = maxRentedMachines;
        this.name = name;
    }

    public String toString() {
        return "Class: " + this.getClass().getSimpleName() + " " + this.getMaxRentedMachines() + " " + this.getName();
    }
}
