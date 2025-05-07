package pl.lodz.p.core.services.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import pl.lodz.p.core.domain.MongoUUID;
import pl.lodz.p.core.domain.Rent;
import pl.lodz.p.core.domain.user.Client;
import pl.lodz.p.core.domain.VMachine;
import pl.lodz.p.core.services.security.JwtTokenProvider;
import pl.lodz.p.infrastructure.rent.*;
import pl.lodz.p.infrastructure.vmachine.VMGet;
import pl.lodz.p.infrastructure.user.UGet;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@AllArgsConstructor
public class RentService {

    private final JwtTokenProvider jwtTokenProvider;

    RentAdd rentAdd;
    RentGet rentGet;
    RentRemove rentRemove;
    RentSize rentSize;
    RentEnd rentEnd;

    VMGet vmGet;
    UGet uGet;

    public Rent createRent(String username, UUID vmID, LocalDateTime startTime) {
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        String username = auth.getName();
        Client client = (Client)uGet.getClientByUsername(username);
        VMachine vm = vmGet.getVMachineByID(new MongoUUID(vmID));
        if(client == null) {
            throw new RuntimeException("Client not found");
        }
        if(vm == null) {
            throw new RuntimeException("VMachine not found");
        }
        if(vm.isRented() > 0){
            throw new RuntimeException("VMachine already rented");
        }
        if(client.getCurrentRents() + 1>client.getClientType().getMaxRentedMachines()){
            throw new RuntimeException("Client is not permitted to rent more machines: " + client.getCurrentRents() + " > " + client.getClientType().getMaxRentedMachines() +1);
        }
        Rent rent = new Rent(client, vm, startTime);
        rentAdd.add(rent);
        return rent;
    }

    public List<Rent> getAllRents() {
        List<Rent> rents = rentGet.getRents();
        if(rents == null || rents.isEmpty()) {
            throw new RuntimeException("Rents not found");
        }
        return rents;
    }

    public List<Rent> getActiveRents() {
        List<Rent> activeRents = rentGet.findBy("endTime", null);
        if(activeRents == null || activeRents.isEmpty()) {
            throw new RuntimeException("Active rents not found");
        }
        return activeRents;
    }

    public List<Rent> getArchivedRents() {
        List<Rent> archivedRents = rentGet.findByNegation("endTime", null);
        if(archivedRents == null || archivedRents.isEmpty()) {
            throw new RuntimeException("Archived rents not found");
        }
        return archivedRents;
    }

    public Rent getRent(UUID uuid) {
        Rent rent = rentGet.getRentByID(new MongoUUID(uuid));
        if(rent == null) {
            throw new RuntimeException("Rent with UUID:" + uuid + " not found");
        }
        return rent;
    }

    public List<Rent> getClientAllRents(String authHeader) {
        String token = authHeader.replace("Bearer ", "");

        String username = jwtTokenProvider.getLogin(token);

        List<Rent> allRents = rentGet.getClientRents(username);

        if (allRents.isEmpty()) {
            throw new RuntimeException("Client with username: " + username + " does not have any rents");
        }

        return allRents;
    }

    public List<Rent> getClientAllRents(UUID uuid) {
        List<Rent> allRents = rentGet.getClientRents(uuid);

        if (allRents.isEmpty()) {
            throw new RuntimeException("Client with UUID: " + uuid + " does not have any rents");
        }

        return allRents;
    }

    public List<Rent> getClientActiveRents(UUID uuid) {
        List<Rent> activeRents = rentGet.getClientRents(new MongoUUID(uuid),true);
        if(activeRents == null || activeRents.isEmpty()) {
            throw new RuntimeException("Client with UUID:" + uuid + " does not have active rents");
        }
        return activeRents;
    }

    public List<Rent> getClientArchivedRents(UUID uuid) {
        List<Rent> archivedRents = rentGet.getClientRents(new MongoUUID(uuid),false);
        if(archivedRents == null || archivedRents.isEmpty()) {
            throw new RuntimeException("Client with UUID:" + uuid + " does not have archived rents");
        }
        return archivedRents;
    }

    public Rent getVMachineActiveRent(UUID uuid) {
        List<Rent> activeRent = rentGet.getVMachineRents(new MongoUUID(uuid),true);
        if(activeRent == null) {
            throw new RuntimeException("VMachine with UUID:" + uuid + " is not currently rented");
        }
        return activeRent.getFirst();
    }

    public List<Rent> getVMachineArchivedRents(UUID uuid) {
        List<Rent> archivedRents = rentGet.getVMachineRents(new MongoUUID(uuid),false);
        if(archivedRents == null || archivedRents.isEmpty()) {
            throw new RuntimeException("VMachine with UUID:" + uuid + " does not have archived rents");
        }
        return archivedRents;
    }

    public void endRent(UUID uuid, LocalDateTime endDate) {
        rentEnd.endRent(new MongoUUID(uuid), endDate);
    }

    public void removeRent(UUID uuid) {
        rentRemove.remove(new MongoUUID(uuid));
    }
}

