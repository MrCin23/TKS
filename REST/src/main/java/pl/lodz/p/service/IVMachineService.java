package pl.lodz.p.service;

import pl.lodz.p.model.VMachine;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface IVMachineService {
    VMachine createVMachine(VMachine vm);

    List<VMachine> getAllVMachines();

    VMachine getVMachine(UUID uuid);

    void updateVMachine(UUID uuid, Map<String, Object> fieldsToUpdate);

    void deleteVMachine(UUID uuid);
}
