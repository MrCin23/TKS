package pl.lodz.p.rest.model;

public class RESTAppleArch extends RESTVMachine {
    public RESTAppleArch(int CPUNumber, String ramSize) {
        super(CPUNumber, ramSize, 0);
        this.actualRentalPrice = getActualRentalPrice();
    }

    public RESTAppleArch() {
        super();
    }

    public RESTAppleArch(RESTMongoUUID uuid, int CPUNumber, String ramSize,
                         int isRented) {
        super(uuid, CPUNumber, ramSize, isRented);

        this.actualRentalPrice = getActualRentalPrice();
    }

//    @Override
//    public float getActualRentalPrice() {
//        float basePrice = 20;
//
//        int ramInGB = Integer.parseInt(getRamSize().replaceAll("[^0-9]", ""));
//
//        if (ramInGB > 4) {
//            int timesDoubled = (int) (Math.log(ramInGB / 4) / Math.log(2));
//            for (int i = 0; i < timesDoubled; i++) {
//                basePrice += basePrice / 2;
//            }
//        }
//        float threadMultiplier = getCPUNumber() / 2.0f;
//
//        return 10 * basePrice * threadMultiplier;
//    }

    public String toString() {
        return "AppleArch: " + this.getEntityId().toString() + " " + this.getCPUNumber() + " " + this.getRamSize() + " " + this.getIsRented() + " " + this.getActualRentalPrice();
    }
}
