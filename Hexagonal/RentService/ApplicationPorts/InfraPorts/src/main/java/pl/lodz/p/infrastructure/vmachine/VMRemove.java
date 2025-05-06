package pl.lodz.p.infrastructure.vmachine;

import org.springframework.stereotype.Component;
import pl.lodz.p.core.domain.VMachine;
@Component
public interface VMRemove {
    void remove(VMachine vMachine);
}
