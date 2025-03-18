package pl.lodz.p.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pl.lodz.p.dto.UuidDTO;
import pl.lodz.p.model.VMachine;
import pl.lodz.p.model.VMachine;
import pl.lodz.p.service.implementation.VMachineService;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@AllArgsConstructor
@RestController
@RequestMapping("/api/vmachine")
@Validated
//@CrossOrigin(origins = {"http://localhost", "https://localhost", "https://flounder-sunny-goldfish.ngrok-free.app", "http://localhost:8080", "http://192.168.1.105", "http://192.168.56.1", "https://192.168.1.105", "https://192.168.56.1"}, allowedHeaders = "*")
//@CrossOrigin(origins = "*", allowedHeaders = "*")
public class VMachineController {

    private VMachineService vMachineService;

    @PostMapping//not tested
    public ResponseEntity<Object> create(@Valid @RequestBody VMachine vm, BindingResult bindingResult) {
        try {
            if(bindingResult.hasErrors()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(bindingResult.getAllErrors());
            }
            return ResponseEntity.status(HttpStatus.CREATED).body(vMachineService.createVMachine(vm));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }

    @GetMapping//not tested
    public ResponseEntity<Object> getAll() {
        try {
            List<VMachine> vms;
            try {
                vms = vMachineService.getAllVMachines();
            } catch (RuntimeException ex) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No vms found");
            }
            return ResponseEntity.status(HttpStatus.OK).body(vms);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }

    }

    @GetMapping("/{uuid}")//not tested
    public ResponseEntity<Object> getVMachine(@PathVariable("uuid") UuidDTO uuid) {
        try {
            VMachine vm;
            try {
                vm = vMachineService.getVMachine(uuid.uuid());
            } catch (RuntimeException ex) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No vm found");
            }
            return ResponseEntity.status(HttpStatus.OK).body(vm);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }

    }

    @PutMapping("/{uuid}")//not tested
    public ResponseEntity<Object> updateVMachine(@PathVariable("uuid") UuidDTO uuid, @RequestBody Map<String, Object> fieldsToUpdate, BindingResult bindingResult) {
        try {
            if(bindingResult.hasErrors()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(bindingResult.getAllErrors());
            }
            vMachineService.updateVMachine(uuid.uuid(), fieldsToUpdate);//tried converting this to dto to validate, bad idea
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }

    }

    @DeleteMapping("/{uuid}")//not tested
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Object> deleteVMachine(@PathVariable("uuid") UuidDTO uuid) {
        try {
            try {
                vMachineService.deleteVMachine(uuid.uuid());
            } catch (RuntimeException ex) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No vm found");
            }
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }
}
