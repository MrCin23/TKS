package pl.lodz.p.rest.adapter;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import pl.lodz.p.core.domain.*;
import pl.lodz.p.core.domain.user.*;
import pl.lodz.p.rest.model.*;
import pl.lodz.p.rest.model.dto.RentDTO;
import pl.lodz.p.core.services.service.RentService;
import pl.lodz.p.rest.model.user.*;
import pl.lodz.p.ui.RentServicePort;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
@AllArgsConstructor
public class RentServiceAdapter implements RentServicePort {

    private final RentService rentService;

    @Override
    public RESTRent createRent(RentDTO rentDTO) {
        return convert(rentService.createRent(rentDTO.getUsername(), rentDTO.getVmId(), rentDTO.getStartTime()));
    }

    @Override
    public List<RESTRent> getAllRents() {
        List<RESTRent> rents = new ArrayList<>();
        for(Rent r : rentService.getAllRents()) {
            rents.add(convert(r));
        }
        return rents;
    }

    @Override
    public List<RESTRent> getActiveRents() {
        List<RESTRent> rents = new ArrayList<>();
        for(Rent r : rentService.getActiveRents()) {
            rents.add(convert(r));
        }
        return rents;
    }

    @Override
    public List<RESTRent> getArchivedRents() {
        List<RESTRent> rents = new ArrayList<>();
        for(Rent r : rentService.getArchivedRents()) {
            rents.add(convert(r));
        }
        return rents;
    }

    @Override
    public RESTRent getRent(UUID id) {
        return convert(rentService.getRent(id));
    }

    @Override
    public List<RESTRent> getClientAllRents(String authHeader) {
        List<RESTRent> rents = new ArrayList<>();
        for(Rent r : rentService.getClientAllRents(authHeader)) {
            rents.add(convert(r));
        }
        return rents;
    }

    @Override
    public List<RESTRent> getClientAllRents(UUID uuid) {
        List<RESTRent> rents = new ArrayList<>();
        for(Rent r : rentService.getClientAllRents(uuid)) {
            rents.add(convert(r));
        }
        return rents;
    }

    @Override
    public List<RESTRent> getClientActiveRents(UUID uuid) {
        List<RESTRent> rents = new ArrayList<>();
        for(Rent r : rentService.getClientActiveRents(uuid)) {
            rents.add(convert(r));
        }
        return rents;
    }

    @Override
    public List<RESTRent> getClientArchivedRents(UUID uuid) {
        List<RESTRent> rents = new ArrayList<>();
        for(Rent r : rentService.getClientArchivedRents(uuid)) {
            rents.add(convert(r));
        }
        return rents;
    }

    @Override
    public RESTRent getVMachineActiveRent(UUID uuid) {
        return convert(rentService.getVMachineActiveRent(uuid));
    }

    @Override
    public List<RESTRent> getVMachineArchivedRents(UUID uuid) {
        List<RESTRent> rents = new ArrayList<>();
        for(Rent r : rentService.getVMachineArchivedRents(uuid)) {
            rents.add(convert(r));
        }
        return rents;
    }

    @Override
    public void endRent(UUID uuid, LocalDateTime endDate) {
        rentService.endRent(uuid, endDate);
    }

    @Override
    public void removeRent(UUID uuid) {
        rentService.removeRent(uuid);
    }

    private Rent convert(RESTRent r) {
        return new Rent(
                convert(r.getEntityId()),
                (Client)convert(r.getClient()),
                convert(r.getVMachine()),
                r.getBeginTime(),
                r.getEndTime(),
                r.getRentCost()
        );
    }

    private RESTRent convert(Rent r) {
        return new RESTRent(
                convert(r.getEntityId()),
                (RESTClient)convert(r.getClient()),
                convert(r.getVMachine()),
                r.getBeginTime(),
                r.getEndTime(),
                r.getRentCost()
        );
    }

    private MongoUUID convert(RESTMongoUUID r) {
        return new MongoUUID(r.getUuid());
    }
    private RESTMongoUUID convert(MongoUUID r) {
        return new RESTMongoUUID(r.getUuid());
    }

    private Role convert(RESTRole ent) {
        if (ent == RESTRole.ADMIN) {
            return Role.ADMIN;
        }
        else if (ent == RESTRole.CLIENT) {
            return Role.CLIENT;
        }
        else if (ent == RESTRole.RESOURCE_MANAGER) {
            return Role.RESOURCE_MANAGER;
        }
        return null;
    }
    private RESTRole convert(Role role) {
        if (role == Role.ADMIN) {
            return RESTRole.ADMIN;
        }
        else if (role == Role.CLIENT) {
            return RESTRole.CLIENT;
        }
        else if (role == Role.RESOURCE_MANAGER) {
            return RESTRole.RESOURCE_MANAGER;
        }
        return null;
    }

    private RESTUser convert(User user) {
        return switch (user.getClass().getSimpleName()) {
            case "Client" -> new RESTClient(convert(user.getEntityId()), user.getFirstName(), user.getUsername(), user.getPassword(),
                    user.getSurname(), user.getEmailAddress(), convert(user.getRole()), user.isActive(), new RESTStandard(), ((Client) user).getCurrentRents());
            case "ResourceManager" -> new RESTResourceManager(convert(user.getEntityId()), user.getFirstName(), user.getSurname(), user.getUsername(), user.getPassword(), user.getEmailAddress());
            case "Admin" -> new RESTAdmin(convert(user.getEntityId()), user.getFirstName(), user.getSurname(), user.getUsername(), user.getEmailAddress(), user.getPassword());
            default -> throw new RuntimeException("Unsupported type: " + user.getClass().getSimpleName());
        };
    }

    private User convert(RESTUser ent) {
        if (ent == null) {
            return null;
        }
        return switch (ent.getClass().getSimpleName()) {
            case "RESTClient" -> new Client(convert(ent.getEntityId()), ent.getFirstName(), ent.getUsername(), ent.getPassword(),
                    ent.getSurname(), ent.getEmailAddress(), convert(ent.getRole()), ent.isActive(), new Standard(), ((RESTClient) ent).getCurrentRents());
            case "RESTResourceManager" -> new ResourceManager(convert(ent.getEntityId()), ent.getFirstName(), ent.getSurname(), ent.getUsername(), ent.getPassword(), ent.getEmailAddress());
            case "RESTAdmin" -> new Admin(convert(ent.getEntityId()), ent.getFirstName(), ent.getSurname(), ent.getUsername(), ent.getEmailAddress(), ent.getPassword());
            default -> throw new RuntimeException("Unsupported type: " + ent.getClass().getSimpleName());
        };
    }

    private RESTVMachine convert(VMachine vm) {
        if (vm == null) {
            return null;
        }
        return switch (vm.getClass().getSimpleName()) {
            case "x86" -> new RESTx86(convert(vm.getEntityId()), vm.getCPUNumber(), vm.getRamSize(), vm.isRented(), ((x86)vm).getManufacturer());
            case "AppleArch" -> new RESTAppleArch(convert(vm.getEntityId()), vm.getCPUNumber(), vm.getRamSize(), vm.isRented());
            default -> throw new RuntimeException(vm.getClass().getSimpleName() + " not supported");
        };
    }

    private VMachine convert(RESTVMachine ent) {
        if (ent == null) {
            return null;
        }
        return switch (ent.getClass().getSimpleName()) {
            case "RESTx86" -> new x86(convert(ent.getEntityId()), ent.getCPUNumber(), ent.getRamSize(), ent.getIsRented(), ((RESTx86)ent).getManufacturer());
            case "RESTAppleArch" -> new AppleArch(convert(ent.getEntityId()), ent.getCPUNumber(), ent.getRamSize(), ent.getIsRented());
            default -> throw new RuntimeException(ent.getClass().getSimpleName() + " not supported");
        };
    }
}
