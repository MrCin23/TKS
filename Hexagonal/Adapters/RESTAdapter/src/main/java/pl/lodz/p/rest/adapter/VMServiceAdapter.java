package pl.lodz.p.rest.adapter;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import pl.lodz.p.core.domain.*;
import pl.lodz.p.core.services.service.VMachineService;
import pl.lodz.p.rest.model.*;
import pl.lodz.p.ui.RentServicePort;
import pl.lodz.p.ui.VMServicePort;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Component
@AllArgsConstructor
public class VMServiceAdapter implements VMServicePort {

    private final VMachineService vMachineService;

    @Override
    public RESTVMachine createVMachine(RESTVMachine vm) {
        return convert(vMachineService.createVMachine(convert(vm)));
    }

    @Override
    public List<RESTVMachine> getAllVMachines() {
        List<RESTVMachine> vms = new ArrayList<>();
        for (VMachine vm : vMachineService.getAllVMachines()) {
            vms.add(convert(vm));
        }
        return vms;
    }

    @Override
    public RESTVMachine getVMachine(UUID uuid) {
        return convert(vMachineService.getVMachine(uuid));
    }

    @Override
    public void updateVMachine(UUID uuid, Map<String, Object> fieldsToUpdate) {
        vMachineService.updateVMachine(uuid, fieldsToUpdate);
    }

    @Override
    public void deleteVMachine(UUID uuid) {
        vMachineService.deleteVMachine(uuid);
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
            case "RESTx86" -> new x86(convert(ent.getEntityId()), ent.getCPUNumber(), ent.getRamSize(), ent.isRented(), ((RESTx86)ent).getManufacturer());
            case "RESTAppleArch" -> new AppleArch(convert(ent.getEntityId()), ent.getCPUNumber(), ent.getRamSize(), ent.isRented());
            default -> throw new RuntimeException(ent.getClass().getSimpleName() + " not supported");
        };
    }

    private MongoUUID convert(RESTMongoUUID r) {
        return new MongoUUID(r.getUuid());
    }
    private RESTMongoUUID convert(MongoUUID r) {
        return new RESTMongoUUID(r.getUuid());
    }
}
