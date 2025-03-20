package pl.lodz.p.port.infrastructure.vmachine;

import pl.lodz.p.core.domain.MongoUUID;
import pl.lodz.p.core.domain.VMachine;

import java.util.List;

public interface VMGet {
    List<VMachine> getVMachines();
    VMachine getVMachineByID(MongoUUID uuid);
}
