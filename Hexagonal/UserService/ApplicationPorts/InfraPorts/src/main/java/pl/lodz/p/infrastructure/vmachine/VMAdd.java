package pl.lodz.p.infrastructure.vmachine;

import org.springframework.stereotype.Component;
import pl.lodz.p.core.domain.VMachine;
@Component
public interface VMAdd {
    void add(VMachine vMachine);
}
