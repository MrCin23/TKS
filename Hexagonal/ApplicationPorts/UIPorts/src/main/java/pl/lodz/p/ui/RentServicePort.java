package pl.lodz.p.ui;

import org.springframework.stereotype.Component;
import pl.lodz.p.rest.model.RESTRent;
import pl.lodz.p.rest.model.dto.RentDTO;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Component
public interface RentServicePort {
    RESTRent createRent(RentDTO rentDTO);

    List<RESTRent> getAllRents();

    List<RESTRent> getActiveRents();

    List<RESTRent> getArchivedRents();

    RESTRent getRent(UUID id);

    List<RESTRent> getClientAllRents(String authHeader);

    List<RESTRent> getClientAllRents(UUID uuid);

    List<RESTRent> getClientActiveRents(UUID uuid);

    List<RESTRent> getClientArchivedRents(UUID uuid);

    RESTRent getVMachineActiveRent(UUID uuid);

    List<RESTRent> getVMachineArchivedRents(UUID uuid);

    void endRent(UUID uuid, LocalDateTime endDate);

    void removeRent(UUID uuid);
}
