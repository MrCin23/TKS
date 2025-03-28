package pl.lodz.p.ui;

import org.springframework.stereotype.Component;
import pl.lodz.p.rest.model.RESTVMachine;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Component
public interface VMServicePort {
    RESTVMachine createVMachine(RESTVMachine vm);

    List<RESTVMachine> getAllVMachines();

    RESTVMachine getVMachine(UUID uuid);

    void updateVMachine(UUID uuid, Map<String, Object> fieldsToUpdate);

    void deleteVMachine(UUID uuid);
}
