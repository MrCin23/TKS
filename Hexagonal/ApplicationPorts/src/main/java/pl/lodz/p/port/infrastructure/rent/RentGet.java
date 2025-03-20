package pl.lodz.p.port.infrastructure.rent;

import pl.lodz.p.core.domain.MongoUUID;
import pl.lodz.p.core.domain.Rent;

import java.util.List;
import java.util.UUID;

public interface RentGet {
    Rent getRentByID(MongoUUID uuid);
    List<Rent> getClientRents(String username);
    List<Rent> getClientRents(UUID uuid);
    List<Rent> getClientRents(MongoUUID clientId, boolean active);
    List<Rent> getVMachineRents(MongoUUID vmId, boolean active);
    List<Rent> getRents();
    List<Rent> findBy(String field, Object value);
    List<Rent> findByNegation(String field, Object value);
}
