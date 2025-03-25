package pl.lodz.p.infrastructure.vmachine;

import org.springframework.stereotype.Component;
import pl.lodz.p.core.domain.MongoUUID;
import pl.lodz.p.core.domain.VMachine;

import java.util.List;
@Component
public interface VMGet {
    List<VMachine> getVMachines();
    VMachine getVMachineByID(MongoUUID uuid);
}
