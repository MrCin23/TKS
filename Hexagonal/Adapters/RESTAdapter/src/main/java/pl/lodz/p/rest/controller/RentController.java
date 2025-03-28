package pl.lodz.p.rest.controller;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import pl.lodz.p.rest.model.RESTRent;
import pl.lodz.p.rest.model.dto.RentDTO;
import pl.lodz.p.rest.model.dto.UuidDTO;
import pl.lodz.p.ui.RentServicePort;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/rent")
//@CrossOrigin(origins = {"http://localhost", "https://localhost", "https://flounder-sunny-goldfish.ngrok-free.app", "http://localhost:8080", "http://192.168.1.105", "http://192.168.56.1", "https://192.168.1.105", "https://192.168.56.1"}, allowedHeaders = "*")
//@CrossOrigin(origins = "*", allowedHeaders = "*")
public class RentController {
    private RentServicePort rentServicePort;

//    @PostMapping//not tested
//    public ResponseEntity<Object> createRent(@Valid @RequestBody RentDTO rentDTO, BindingResult bindingResult) {
//        try {
//            Rent pom = rentServicePort.createRent(rentDTO);
//            if (bindingResult.hasErrors()) {
//                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(bindingResult.getAllErrors());
//            }
//            return ResponseEntity.status(HttpStatus.CREATED).body(pom);
//        } catch (RuntimeException e) {
//            return ResponseEntity.status(HttpStatus.CONFLICT).body(e);
//        } catch (Exception ex) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
//        }
//    }

    @PostMapping
    public ResponseEntity<Object> createRent(@Valid @RequestBody RentDTO rentDTO, BindingResult bindingResult, HttpServletRequest request) {
        try {
            if (bindingResult.hasErrors()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(bindingResult.getAllErrors());
            }
            return ResponseEntity.status(HttpStatus.CREATED).body(rentServicePort.createRent(rentDTO));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }

    @GetMapping//not tested
    public ResponseEntity<Object> getAllRents() {
        try {
            List<RESTRent> rents;
            try {
                rents = rentServicePort.getAllRents();
            } catch (RuntimeException ex) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No rents found");
            }
            return ResponseEntity.status(HttpStatus.OK).body(rents);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }

    @GetMapping("/{uuid}")//not tested
    public ResponseEntity<Object> getRent(@PathVariable("uuid") UuidDTO uuid) {
        try {
            RESTRent rent;
            try {
                rent = rentServicePort.getRent(uuid.uuid());
            } catch (RuntimeException ex) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No rent found");
            }
            return ResponseEntity.status(HttpStatus.OK).body(rent);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }

    @GetMapping("/active")//not tested
    public ResponseEntity<Object> getActiveRents() {
        try {
            List<RESTRent> rents;
            try {
                rents = rentServicePort.getActiveRents();
            } catch (RuntimeException ex) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No rents found");
            }
            return ResponseEntity.status(HttpStatus.OK).body(rents);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }

    @GetMapping("/archived")//not tested
    public ResponseEntity<Object> getArchivedRents() {
        try {
            List<RESTRent> rents;
            try {
                rents = rentServicePort.getArchivedRents();
            } catch (RuntimeException ex) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No rents found");
            }
            return ResponseEntity.status(HttpStatus.OK).body(rents);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }

    @PutMapping("/end/{uuid}")//not tested
    public ResponseEntity<Object> endRent(@PathVariable("uuid") UuidDTO uuid) { //, @Valid @RequestBody EndRentDTO endTimeDTO, BindingResult bindingResult
        try {
            try{
                LocalDateTime endDate = LocalDateTime.now();
                rentServicePort.endRent(uuid.uuid(), endDate); //endTimeDTO.getEndTime()
            } catch (RuntimeException ex) { 
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
            }
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }

    }

//    @GetMapping("/all/client/{uuid}")//not tested
//    public ResponseEntity<Object> getClientAllRents(@PathVariable("uuid") UuidDTO uuid){
//        try {
//            List<Rent> rents;
//            try {
//                rents = rentServicePort.getClientAllRents(uuid.uuid());
//            } catch (RuntimeException ex) {
//                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No rents found");
//            }
//            return ResponseEntity.status(HttpStatus.OK).body(rents);
//        } catch (Exception ex) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
//        }
//    }

    @GetMapping("/all/client")
    public ResponseEntity<Object> getClientAllRents(@RequestHeader("Authorization") String authHeader) {
        try {
            List<RESTRent> rents = rentServicePort.getClientAllRents(authHeader);//todo here
            return ResponseEntity.ok(rents);
        } catch (ExpiredJwtException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token expired");
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/all/client/{uuid}")
    public ResponseEntity<Object> getClientAllRentsByUUID(@PathVariable("uuid") UuidDTO uuid) {
        try {
            List<RESTRent> rents = rentServicePort.getClientAllRents(uuid.uuid());
            return ResponseEntity.ok(rents);
        } catch (ExpiredJwtException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token expired");
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }


    @GetMapping("/active/client/{uuid}")//not tested
    public ResponseEntity<Object> getClientActiveRents(@PathVariable("uuid") UuidDTO uuid){
        try {
            List<RESTRent> rents;
            try {
                rents = rentServicePort.getClientActiveRents(uuid.uuid());
            } catch (RuntimeException ex) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No rents found");
            }
            return ResponseEntity.status(HttpStatus.OK).body(rents);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }

    @GetMapping("/active/vmachine/{uuid}")//not tested
    public ResponseEntity<Object> getVMachineActiveRent(@PathVariable("uuid") UuidDTO uuid){
        try {
            RESTRent rent;
            try {
                rent = rentServicePort.getVMachineActiveRent(uuid.uuid());
            } catch (RuntimeException ex) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No rents found");
            }
            return ResponseEntity.status(HttpStatus.OK).body(rent);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }

    @GetMapping("/archived/client/{uuid}")//not tested
    public ResponseEntity<Object> getClientArchivedRents(@PathVariable("uuid") UuidDTO uuid){
        try {
            List<RESTRent> rents;
            try {
                rents = rentServicePort.getClientArchivedRents(uuid.uuid());
            } catch (RuntimeException ex) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No rents found");
            }
            return ResponseEntity.status(HttpStatus.OK).body(rents);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }

    @GetMapping("/archived/vmachine/{uuid}")//not tested
    public ResponseEntity<Object> getVMachineArchivedRents(@PathVariable("uuid") UuidDTO uuid){
        try {
            List<RESTRent> rents;
            try {
                rents = rentServicePort.getVMachineArchivedRents(uuid.uuid());
            } catch (RuntimeException ex) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No rents found");
            }
            return ResponseEntity.status(HttpStatus.OK).body(rents);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }

    @DeleteMapping("/{uuid}")//not tested
    public ResponseEntity<Object> deleteRent(@PathVariable("uuid") UuidDTO uuid) {
        try {
            try {
                rentServicePort.removeRent(uuid.uuid());
            } catch (RuntimeException ex) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No rents found");
            }
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }
}
