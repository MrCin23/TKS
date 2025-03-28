package pl.lodz.p.core.services.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.lodz.p.core.domain.MongoUUID;
import pl.lodz.p.core.domain.VMachine;
//import pl.lodz.p.repository.VMachineRepository;
import pl.lodz.p.infrastructure.vmachine.VMAdd;
import pl.lodz.p.infrastructure.vmachine.VMGet;
import pl.lodz.p.infrastructure.vmachine.VMRemove;
import pl.lodz.p.infrastructure.vmachine.VMUpdate;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@AllArgsConstructor
public class VMachineService {

    VMGet vmget;
    VMAdd vmadd;
    VMRemove vmremove;
    VMUpdate vmupdate;

    public VMachine createVMachine(VMachine vm) {
        if(vmget.getVMachineByID(vm.getEntityId()) == null) {
            vmadd.add(vm);
            return vm;
        }
        throw new RuntimeException("VMachine with id " + vm.getEntityId() + " already exists");
    }

    public List<VMachine> getAllVMachines() {
        List<VMachine> vms = vmget.getVMachines();
        if(vms == null || vms.isEmpty()) {
            throw new RuntimeException("No vmachines found");
        }
        return vmget.getVMachines();
    }

    public VMachine getVMachine(UUID uuid) {
        VMachine vm = vmget.getVMachineByID(new MongoUUID(uuid));
        if(vm == null) {
            throw new RuntimeException("VMachine with id " + uuid + " does not exist");
        }
        return vmget.getVMachineByID(new MongoUUID(uuid));
    }

    public void updateVMachine(UUID uuid, Map<String, Object> fieldsToUpdate) {
        if(vmget.getVMachineByID(new MongoUUID(uuid)) == null) {
            throw new RuntimeException("VMachine with id " + uuid + " does not exist");
        }
        vmupdate.update(new MongoUUID(uuid), fieldsToUpdate);
    }

    public void deleteVMachine(UUID uuid) {
        if(vmget.getVMachineByID(new MongoUUID(uuid)) == null) {
            throw new RuntimeException("VMachine with id " + uuid + " does not exist");
        }
        if(vmget.getVMachineByID(new MongoUUID(uuid)).getIsRented() > 0) {
            throw new RuntimeException("VMachine with id " + uuid + " is currently rented");
        }
        vmremove.remove(vmget.getVMachineByID(new MongoUUID(uuid)));
    }
}
