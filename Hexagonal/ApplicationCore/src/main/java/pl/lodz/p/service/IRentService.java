package pl.lodz.p.service;

import pl.lodz.p.dto.RentDTO;
import pl.lodz.p.model.Rent;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface IRentService {

    Rent createRent(RentDTO rentDTO);

    List<Rent> getAllRents();

    List<Rent> getActiveRents();

    List<Rent> getArchivedRents();

    Rent getRent(UUID id);

//    List<Rent> getClientAllRents(UUID uuid);
    List<Rent> getClientAllRents(String authHeader);
    List<Rent> getClientAllRents(UUID uuid);

    List<Rent> getClientActiveRents(UUID uuid);

    List<Rent> getClientArchivedRents(UUID uuid);

    Rent getVMachineActiveRent(UUID uuid);

    List<Rent> getVMachineArchivedRents(UUID uuid);

    void endRent(UUID uuid, LocalDateTime endDate);

    void removeRent(UUID uuid);
}
