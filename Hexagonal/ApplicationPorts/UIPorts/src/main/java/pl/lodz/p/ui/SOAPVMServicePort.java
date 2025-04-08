package pl.lodz.p.ui;

import org.springframework.stereotype.Component;
import pl.lodz.p.rest.model.RESTVMachine;
import pl.lodz.p.vmachines.VMachineSOAP;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface SOAPVMServicePort {
    VMachineSOAP createVMachine(VMachineSOAP vm);

    List<VMachineSOAP> getAllVMachines();

    VMachineSOAP getVMachine(UUID uuid);

    void updateVMachine(UUID uuid, Map<String, Object> fieldsToUpdate);

    void deleteVMachine(UUID uuid);
}
