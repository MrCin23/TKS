package pl.lodz.p.repo;

import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonDiscriminator;
import org.bson.codecs.pojo.annotations.BsonProperty;

@BsonDiscriminator(value="applearch", key="_clazz")
public class AppleArchEnt extends VMachineEnt {
    public AppleArchEnt(int CPUNumber, String ramSize) {
        super(CPUNumber, ramSize, 0);
        this.actualRentalPrice = getActualRentalPrice();
    }

    public AppleArchEnt() {
        super();
    }

    @BsonCreator
    public AppleArchEnt(@BsonProperty("_id") MongoUUIDEnt uuid, @BsonProperty("CPUNumber") int CPUNumber, @BsonProperty("ramSize") String ramSize,
                        @BsonProperty("isRented") int isRented) {
        super(uuid, CPUNumber, ramSize, isRented);

        this.actualRentalPrice = getActualRentalPrice();
    }

    @Override
    public float getActualRentalPrice() {
        float basePrice = 20;

        int ramInGB = Integer.parseInt(getRamSize().replaceAll("[^0-9]", ""));

        if (ramInGB > 4) {
            int timesDoubled = (int) (Math.log(ramInGB / 4) / Math.log(2));
            for (int i = 0; i < timesDoubled; i++) {
                basePrice += basePrice / 2;
            }
        }
        float threadMultiplier = getCPUNumber() / 2.0f;

        return 10 * basePrice * threadMultiplier;
    }

    public String toString() {
        return "AppleArch: " + this.getEntityId().toString() + " " + this.getCPUNumber() + " " + this.getRamSize() + " " + this.isRented() + " " + this.getActualRentalPrice();
    }
}
