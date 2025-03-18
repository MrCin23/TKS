package pl.lodz.p.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pl.lodz.p.dto.ChangePasswordDTO;
import pl.lodz.p.dto.LoginDTO;
import pl.lodz.p.dto.UuidDTO;
import pl.lodz.p.exception.DeactivatedUserException;
import pl.lodz.p.exception.WrongPasswordException;
import pl.lodz.p.model.user.Client;
import pl.lodz.p.model.user.User;
import pl.lodz.p.security.JwsProvider;
import pl.lodz.p.security.JwtTokenProvider;
import pl.lodz.p.service.IUserService;
import pl.lodz.p.service.implementation.UserService;

import java.util.List;
import java.util.Map;

@AllArgsConstructor
@RestController
@RequestMapping("/api/client")
@Validated
//@CrossOrigin(origins = {"http://localhost", "https://localhost", "https://flounder-sunny-goldfish.ngrok-free.app", "http://localhost:8080", "http://192.168.1.105", "http://192.168.56.1", "https://192.168.1.105", "https://192.168.56.1"}, allowedHeaders = "*")
//@CrossOrigin(origins = "*", allowedHeaders = "*")
public class UserController {

    private JwsProvider jwsProvider;
    private IUserService clientServiceImplementation;

    @PostMapping//tested
    public ResponseEntity<Object> createUser(@Valid @RequestBody User user, BindingResult bindingResult) {
        try {
            if (bindingResult.hasErrors()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(bindingResult.getAllErrors());
            }
            try {
                return ResponseEntity.status(HttpStatus.CREATED).body(clientServiceImplementation.createUser(user));
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
            }
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("User with username " + user.getUsername() + " already exists! Error code: " + ex);
        }
    }

    @GetMapping//tested
    public ResponseEntity<Object> getAllUsers() {
        try {
            List<User> users;
            try {
                users = clientServiceImplementation.getAllUsers();
            } catch (RuntimeException ex) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No users found");
            }
            return ResponseEntity.status(HttpStatus.OK).body(users);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<Object> getUser(@PathVariable("uuid") UuidDTO uuid) {
        try {
            User user;
            try {
                user = clientServiceImplementation.getUser(uuid.uuid());
            } catch (RuntimeException ex) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No users found");
            }
            return ResponseEntity.status(HttpStatus.OK).header("etag", jwsProvider.generateJws(uuid.getUuid(), user.getUsername())).body(user);

        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }

    @PutMapping("/{uuid}")
    public ResponseEntity<Object> updateUser(@PathVariable("uuid") UuidDTO uuid,
                                             @RequestBody Map<String, Object> fieldsToUpdate,
                                             @RequestHeader(value = "If-Match", required = false) String ifMatchHeader,
                                             BindingResult bindingResult) {
        try {
            if (bindingResult.hasErrors()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(bindingResult.getAllErrors());
            }
            User user = clientServiceImplementation.getUser(uuid.uuid());

            if (ifMatchHeader == null || !jwsProvider.validateJws(ifMatchHeader, user.getEntityId().getUuid().toString(), user.getUsername())) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body("ETag mismatch: Resource has been modified since you last fetched it.");
            }
            clientServiceImplementation.updateUser(uuid.uuid(), fieldsToUpdate);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("User with uuid " + uuid.uuid() + " has been updated");

        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }

    @PutMapping("/deactivate/{uuid}")//tested
    public ResponseEntity<Object> deactivateUser(@PathVariable("uuid") UuidDTO uuid) {
        try {
            try {
                clientServiceImplementation.deactivateUser(uuid.uuid());
            } catch (RuntimeException ex) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No user found");
            }
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("User with uuid " + uuid.uuid() + " has been deactivated");
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }

    @PutMapping("/activate/{uuid}")//tested
    public ResponseEntity<Object> activateUser(@PathVariable("uuid") UuidDTO uuid) {
        try {
            try {
                clientServiceImplementation.activateUser(uuid.uuid());
            } catch (RuntimeException ex) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No user found");
            }
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("User with uuid " + uuid.uuid() + " has been activated");
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Object> findUser(@RequestBody @Valid LoginDTO loginDTO) {
        try {
            String token;
            try {
                token = clientServiceImplementation.getUserByUsername(loginDTO);
            } catch (DeactivatedUserException ex) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("User is deactivated");
            } catch (WrongPasswordException e) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
            } catch (RuntimeException ex) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No users found");
            }
            return ResponseEntity.status(HttpStatus.OK).body(token);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }

    @GetMapping("/findClient/{username}")//tested
    public ResponseEntity<Object> findUser(@PathVariable("username") String username) {
        try {
            User users;
            try {
                users = clientServiceImplementation.getUserByUsername(username);
            } catch (RuntimeException ex) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No users matching");
            }
            return ResponseEntity.status(HttpStatus.OK).header("etag", jwsProvider.generateJws(users.getEntityId().getUuid().toString(), username) ).body(users);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }

    @GetMapping("/findClients/{username}")//tested
    public ResponseEntity<Object> findUsers(@PathVariable("username") String username) {
        try {
            List<User> users;
            try {
                users = clientServiceImplementation.getUsersByUsername(username);
            } catch (RuntimeException ex) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No users matching");
            }
            return ResponseEntity.status(HttpStatus.OK).body(users);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<Object> logout(HttpServletRequest request) { // to jest chyba git
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            clientServiceImplementation.invalidateToken(bearerToken.substring(7));
            return ResponseEntity.status(HttpStatus.OK).build();
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @PutMapping("/changePassword")
    public ResponseEntity<Object> changePassword(@RequestBody @Valid ChangePasswordDTO changePasswordDTO) {
        try {
            String token;
            try {
                token = clientServiceImplementation.getUserByUsername(new LoginDTO(changePasswordDTO.getUsername(), changePasswordDTO.getOldPassword()));
                clientServiceImplementation.changePassword(changePasswordDTO.getUsername(), changePasswordDTO.getNewPassword());
            } catch (WrongPasswordException e) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
            } catch (RuntimeException ex) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No users found");
            }
            return ResponseEntity.status(HttpStatus.OK).body(token);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }
}