package pl.lodz.p.repo.model;


import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonDiscriminator;
import org.bson.codecs.pojo.annotations.BsonProperty;

@Getter
@Setter
@BsonDiscriminator(value="x86", key="_clazz")
public class x86Ent extends VMachineEnt {
    @BsonProperty("manufacturer")
    @NotNull(message = "CPU manufacturer cannot be null")
    private String manufacturer;

    public x86Ent(int CPUNumber, String ramSize, String manufacturer) {
        super(CPUNumber, ramSize, 0);
        this.manufacturer = manufacturer;
        this.actualRentalPrice = getActualRentalPrice();
    }

    public x86Ent() {
        super();
    }

    @BsonCreator
    public x86Ent(@BsonProperty("_id") MongoUUIDEnt uuid, @BsonProperty("CPUNumber") int CPUNumber, @BsonProperty("ramSize") String ramSize,
                  @BsonProperty("isRented") int isRented, @BsonProperty("manufacturer") String manufacturer) {
        super(uuid, CPUNumber, ramSize, isRented);
        this.manufacturer = manufacturer;
        this.actualRentalPrice = getActualRentalPrice();
    }

    @Override
    public float getActualRentalPrice() {

        float basePrice = 10;
        int ramInGB = Integer.parseInt(getRamSize().replaceAll("[^0-9]", ""));
        if (ramInGB > 4) {
            int timesDoubled = (int) (Math.log(ramInGB / 4) / Math.log(2));
            for (int i = 0; i < timesDoubled; i++) {
                basePrice += basePrice / 2;
            }
        }

        float threadMultiplier = getCPUNumber() / 2.0f;

        float manufacturerMultiplier = 1;
        if (manufacturer.equalsIgnoreCase("Intel")) {
            manufacturerMultiplier = 3;
        } else if (manufacturer.equalsIgnoreCase("AMD")) {
            manufacturerMultiplier = 2;
        }

        return basePrice * threadMultiplier * manufacturerMultiplier;
    }

    public String toString() {
        return "x86 architecture: " + this.getEntityId().toString() + " " + this.getCPUNumber() + " " + this.getRamSize() + " " + this.isRented() + " " + this.getManufacturer() + " " + this.getActualRentalPrice();
    }
}
