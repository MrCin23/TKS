package pl.lodz.p.repo.adapter;

import lombok.AllArgsConstructor;
import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.stereotype.Component;
import pl.lodz.p.core.domain.MongoUUID;
import pl.lodz.p.core.domain.Rent;
import pl.lodz.p.port.infrastructure.rent.*;
import pl.lodz.p.repo.model.MongoUUIDEnt;
import pl.lodz.p.repo.model.RentEnt;
import pl.lodz.p.repo.repository.RentRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
@AllArgsConstructor
public class RentAdapter implements RentAdd, RentEnd, RentGet, RentRemove, RentSize {
    RentRepository rentRepo;

    @Override
    public void add(Rent rent) {
        rentRepo.add(convert(rent));
    }

    @Override
    public void endRent(MongoUUID uuid, LocalDateTime endTime) {
        rentRepo.endRent(convert(uuid), endTime);
    }

    @Override
    public Rent getRentByID(MongoUUID uuid) {
        return convert(rentRepo.getRentByID(convert(uuid)));
    }

    @Override
    public List<Rent> getClientRents(String username) {
        List<Rent> result = new ArrayList<>();
        for(RentEnt ent : rentRepo.getClientRents(username)) {
            result.add(convert(ent));
        }
        return result;
    }

    @Override
    public List<Rent> getClientRents(UUID uuid) {
        List<Rent> result = new ArrayList<>();
        for(RentEnt ent : rentRepo.getClientRents(uuid)) {
            result.add(convert(ent));
        }
        return result;
    }

    @Override
    public List<Rent> getClientRents(MongoUUID clientId, boolean active) {
        List<Rent> result = new ArrayList<>();
        for(RentEnt ent : rentRepo.getClientRents(convert(clientId), active)) {
            result.add(convert(ent));
        }
        return result;
    }

    @Override
    public List<Rent> getVMachineRents(MongoUUID vmId, boolean active) {
        List<Rent> result = new ArrayList<>();
        for(RentEnt ent : rentRepo.getVMachineRents(convert(vmId), active)) {
            result.add(convert(ent));
        }
        return result;
    }

    @Override
    public List<Rent> getRents() {
        List<Rent> result = new ArrayList<>();
        for(RentEnt ent : rentRepo.getRents()) {
            result.add(convert(ent));
        }
        return result;
    }

    @Override
    public List<Rent> findBy(String field, Object value) {
        List<Rent> result = new ArrayList<>();
        for(RentEnt ent : rentRepo.findBy(field, value)) {
            result.add(convert(ent));
        }
        return result;
    }

    @Override
    public List<Rent> findByNegation(String field, Object value) {
        List<Rent> result = new ArrayList<>();
        for(RentEnt ent : rentRepo.findByNegation(field, value)) {
            result.add(convert(ent));
        }
        return result;
    }

    @Override
    public void remove(MongoUUID uuid) {
        rentRepo.remove(convert(uuid));
    }

    @Override
    public long size() {
        return rentRepo.size();
    }

    private Rent convert(RentEnt ent) {
        Rent rent = new Rent();
        try {
            PropertyUtils.copyProperties(rent, ent);
        } catch (IllegalAccessException | java.lang.reflect.InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException("Property copying failed: " + e);
        }
        return rent;
    }

    private RentEnt convert(Rent rent) {
        RentEnt ent = new RentEnt();
        try {
            PropertyUtils.copyProperties(ent, rent);
        } catch (IllegalAccessException | java.lang.reflect.InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException("Property copying failed: " + e);
        }
        return ent;
    }

    private MongoUUID convert(MongoUUIDEnt ent) {
        MongoUUID uuid = new MongoUUID();
        try {
            PropertyUtils.copyProperties(uuid, ent);
        } catch (IllegalAccessException | java.lang.reflect.InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException("Property copying failed: " + e);
        }
        return uuid;
    }

    private MongoUUIDEnt convert(MongoUUID uuid) {
        MongoUUIDEnt ent = new MongoUUIDEnt();
        try {
            PropertyUtils.copyProperties(ent, uuid);
        } catch (IllegalAccessException | java.lang.reflect.InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException("Property copying failed: " + e);
        }
        return ent;
    }
}
