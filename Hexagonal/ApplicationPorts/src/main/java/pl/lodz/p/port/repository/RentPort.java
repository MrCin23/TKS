package pl.lodz.p.port.repository;

import pl.lodz.p.model.MongoUUID;
import pl.lodz.p.model.Rent;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface RentPort {
    void endRent(MongoUUID uuid, LocalDateTime endTime);

    void update(long id, Map<String, Object> fieldsToUpdate);

    void add(Rent rent);

    long size(boolean active);

    long size();

    List<Rent> getRents(boolean active);

    List<Rent> getClientRents(MongoUUID clientId);

    List<Rent> getClientRents(String username);

    List<Rent> getClientRents(UUID uuid);

    List<Rent> getClientRents(MongoUUID clientId, boolean active);

    List<Rent> getVMachineRents(MongoUUID vmId, boolean active);

    List<Rent> getRents();

    Rent getRentByID(MongoUUID uuid);

    List<Rent> findBy(String field, Object value);

    List<Rent> findByNegation(String field, Object value);

    void remove(MongoUUID uuid);
}
