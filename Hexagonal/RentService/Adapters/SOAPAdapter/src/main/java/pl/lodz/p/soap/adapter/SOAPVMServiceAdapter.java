package pl.lodz.p.soap.adapter;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import pl.lodz.p.core.domain.AppleArch;
import pl.lodz.p.core.domain.MongoUUID;
import pl.lodz.p.core.domain.VMachine;
import pl.lodz.p.core.domain.x86;
import pl.lodz.p.core.services.service.VMachineService;
import pl.lodz.p.rest.model.RESTMongoUUID;
import pl.lodz.p.ui.SOAPVMServicePort;
import pl.lodz.p.vmachines.AppleArchSOAP;
import pl.lodz.p.vmachines.VMachineSOAP;
import pl.lodz.p.vmachines.X86SOAP;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Component
@AllArgsConstructor
public class SOAPVMServiceAdapter implements SOAPVMServicePort {

    private final VMachineService vMachineService;

    @Override
    public VMachineSOAP createVMachine(VMachineSOAP vm) {
        return convert(vMachineService.createVMachine(convert(vm)));
    }

    @Override
    public List<VMachineSOAP> getAllVMachines() {
        List<VMachineSOAP> vms = new ArrayList<>();
        for (VMachine vm : vMachineService.getAllVMachines()) {
            vms.add(convert(vm));
        }
        return vms;
    }

    @Override
    public VMachineSOAP getVMachine(UUID uuid) {
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

    private VMachineSOAP convert(VMachine vm) {
        if (vm == null) {
            return null;
        }
        VMachineSOAP vMachineSOAP = switch (vm.getClass().getSimpleName()) {
            case "x86" -> new X86SOAP();
            case "AppleArch" -> new AppleArchSOAP();
            default -> throw new RuntimeException(vm.getClass().getSimpleName() + " not supported");
        };
        vMachineSOAP.setRamSize(vm.getRamSize());
        vMachineSOAP.setCPUNumber(vm.getCPUNumber());
        vMachineSOAP.setEntityId(vm.getEntityId().getUuid().toString());
        vMachineSOAP.setIsRented(vm.getIsRented());
        vMachineSOAP.setActualRentalPrice(vm.getActualRentalPrice());
        return vMachineSOAP;
    }

    private VMachine convert(VMachineSOAP ent) {
        if (ent == null) {
            return null;
        }
        return switch (ent.getClass().getSimpleName()) {
            case "X86SOAP" -> new x86(new MongoUUID(UUID.fromString(ent.getEntityId())), ent.getCPUNumber(), ent.getRamSize(), ent.getIsRented(), ((X86SOAP)ent).getManufacturer());
            case "AppleArchSOAP" -> new AppleArch(new MongoUUID(UUID.fromString(ent.getEntityId())), ent.getCPUNumber(), ent.getRamSize(), ent.getIsRented());
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
