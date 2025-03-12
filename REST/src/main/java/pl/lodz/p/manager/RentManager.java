package pl.lodz.p.manager;

import lombok.Getter;
import pl.lodz.p.model.user.Client;
import pl.lodz.p.model.MongoUUID;
import pl.lodz.p.model.Rent;
import pl.lodz.p.model.VMachine;
import pl.lodz.p.repository.RentRepository;

import java.time.LocalDateTime;

//RentManager jako Singleton
@Getter
public final class RentManager {
    private static RentManager instance;
    private final RentRepository rentRepository;

    public RentManager() {
        rentRepository = new RentRepository();
    }

    public static RentManager getInstance() {
        if (instance == null) {
            instance = new RentManager();
        }
        return instance;
    }

    public void registerExistingRent(Rent rent) {
        rentRepository.add(rent);
    }

    public void registerRent(Client client, VMachine vMachine, LocalDateTime beginTime) {
        Rent rent = new Rent(client, vMachine, beginTime);
        registerExistingRent(rent);
    }

    public void endRent(MongoUUID id, LocalDateTime endTime) {
        rentRepository.endRent(id, endTime);
    }

    //METHODS-----------------------------------
    public String getAllRentsReport() {
        return this.rentRepository.getRents().toString();
    }
    public Rent getRent(MongoUUID uuid) {
        return rentRepository.getRentByID(uuid);
    }
    public int size(){
        return rentRepository.getRents().size();
    }
}

