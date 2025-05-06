package pl.lodz.p.infrastructure.rent;

import org.springframework.stereotype.Component;
import pl.lodz.p.core.domain.Rent;

@Component
public interface RentAdd {
    void add(Rent rent);
}
