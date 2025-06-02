package pl.lodz.p.rest.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pl.lodz.p.rest.aspect.Counted;
import pl.lodz.p.rest.config.JwsProvider;
import pl.lodz.p.rest.model.dto.UuidDTO;

import pl.lodz.p.rest.model.user.RESTClient;
import pl.lodz.p.ui.RESTClientServicePort;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/client")
@Validated
@Counted
//@CrossOrigin(origins = {"http://localhost", "https://localhost", "https://flounder-sunny-goldfish.ngrok-free.app", "http://localhost:8080", "http://192.168.1.105", "http://192.168.56.1", "https://192.168.1.105", "https://192.168.56.1"}, allowedHeaders = "*")
//@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ClientController {

    private JwsProvider jwsProvider;
    @Qualifier("RESTClientServicePort")
    private RESTClientServicePort userServicePort;

    @PostMapping//tested
    public ResponseEntity<Object> createClient(@Valid @RequestBody RESTClient user, BindingResult bindingResult) {
        try {
            if (bindingResult.hasErrors()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(bindingResult.getAllErrors());
            }
            try {
                return ResponseEntity.status(HttpStatus.CREATED).body(userServicePort.createClient(user));
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
            }
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Client with username " + user.getUsername() + " already exists! Error code: " + ex);
        }
    }

    @GetMapping//tested
    public ResponseEntity<Object> getAllClients() {
        try {
            List<RESTClient> users;
            try {
                users = userServicePort.getAllClients();
            } catch (RuntimeException ex) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No users found");
            }
            return ResponseEntity.status(HttpStatus.OK).body(users);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<Object> getClient(@PathVariable("uuid") UuidDTO uuid) {
        try {
            RESTClient user;
            try {
                user = userServicePort.getClient(uuid.uuid());
            } catch (RuntimeException ex) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No users found");
            }
            return ResponseEntity.status(HttpStatus.OK).header("etag", jwsProvider.generateJws(uuid.getUuid(), user.getUsername())).body(user);

        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }


    @GetMapping("/findClient/{username}")//tested
    public ResponseEntity<Object> findClient(@PathVariable("username") String username) {
        try {
            RESTClient users;
            try {
                users = userServicePort.getClientByUsername(username);
            } catch (RuntimeException ex) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No users matching");
            }
            return ResponseEntity.status(HttpStatus.OK).header("etag", jwsProvider.generateJws(users.getEntityId().getUuid().toString(), username) ).body(users);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }

    @GetMapping("/findClients/{username}")//tested
    public ResponseEntity<Object> findClients(@PathVariable("username") String username) {
        try {
            List<RESTClient> users;
            try {
                users = userServicePort.getClientsByUsername(username);
            } catch (RuntimeException ex) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No users matching");
            }
            return ResponseEntity.status(HttpStatus.OK).body(users);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }


}