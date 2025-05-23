package pl.lodz.p.repo.rent.adapter;

import lombok.AllArgsConstructor;
import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.stereotype.Component;
import pl.lodz.p.core.domain.*;
import pl.lodz.p.core.domain.user.*;
import pl.lodz.p.infrastructure.rent.*;
import pl.lodz.p.repo.MongoUUIDEnt;
import pl.lodz.p.repo.rent.data.RentEnt;
import pl.lodz.p.repo.rent.repo.RentRepository;
import pl.lodz.p.repo.client.data.ClientEnt;
import pl.lodz.p.repo.client.data.RoleEnt;
import pl.lodz.p.repo.client.data.StandardEnt;
import pl.lodz.p.repo.vm.data.AppleArchEnt;
import pl.lodz.p.repo.vm.data.VMachineEnt;
import pl.lodz.p.repo.vm.data.x86Ent;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
@Component
@AllArgsConstructor
public class RentAdapter implements RentAdd, RentEnd, RentGet, RentRemove, RentSize {
    private final RentRepository rentRepo;

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
        if(ent == null) {
            return null;
        }
        Client client = new Client(convert(ent.getEntityId()), ent.getClientEnt().getUsername(), new Standard(), ent.getClientEnt().getCurrentRents(), ent.getClientEnt().isActive());
        VMachine vMachine = switch (ent.getVMachine().getClass().getSimpleName()) {
            case "x86Ent" -> new x86(convert(ent.getVMachine().getEntityId()), ent.getVMachine().getCPUNumber(), ent.getVMachine().getRamSize(), ent.getVMachine().isRented(), ((x86Ent)ent.getVMachine()).getManufacturer());
            case "AppleArchEnt" -> new AppleArch(convert(ent.getVMachine().getEntityId()), ent.getVMachine().getCPUNumber(), ent.getVMachine().getRamSize(), ent.getVMachine().isRented());
            default -> throw new RuntimeException(ent.getVMachine().getClass().getSimpleName() + " not supported");
        };
        return new Rent(convert(ent.getEntityId()), (Client) client, vMachine, ent.getBeginTime(), ent.getEndTime(), ent.getRentCost());
    }

    private RentEnt convert(Rent rent) {
        if(rent == null) {
            return null;
        }
        ClientEnt user = new ClientEnt(convert(rent.getClient().getEntityId()), rent.getClient().getUsername(), rent.getClient().isActive(), new StandardEnt(), rent.getClient().getCurrentRents());
        VMachineEnt vMachine = switch (rent.getVMachine().getClass().getSimpleName()) {
            case "x86" -> new x86Ent(convert(rent.getVMachine().getEntityId()), rent.getVMachine().getCPUNumber(), rent.getVMachine().getRamSize(), rent.getVMachine().isRented(), ((x86)rent.getVMachine()).getManufacturer());
            case "AppleArch" -> new AppleArchEnt(convert(rent.getVMachine().getEntityId()), rent.getVMachine().getCPUNumber(), rent.getVMachine().getRamSize(), rent.getVMachine().isRented());
            default -> throw new RuntimeException(rent.getVMachine().getClass().getSimpleName() + " not supported");
        };
        return new RentEnt(convert(rent.getEntityId()), (ClientEnt) user, vMachine, rent.getBeginTime(), rent.getEndTime(), rent.getRentCost());

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

    private Role convert(RoleEnt ent) {
        if (ent == RoleEnt.ADMIN) {
            return Role.ADMIN;
        }
        else if (ent == RoleEnt.CLIENT) {
            return Role.CLIENT;
        }
        else if (ent == RoleEnt.RESOURCE_MANAGER) {
            return Role.RESOURCE_MANAGER;
        }
        return null;
    }

    private RoleEnt convert(Role role) {
        if (role == Role.ADMIN) {
            return RoleEnt.ADMIN;
        }
        else if (role == Role.CLIENT) {
            return RoleEnt.CLIENT;
        }
        else if (role == Role.RESOURCE_MANAGER) {
            return RoleEnt.RESOURCE_MANAGER;
        }
        return null;
    }
}
