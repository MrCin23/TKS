package unit.controllers;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import pl.lodz.p.controller.UserController;
import pl.lodz.p.dto.UuidDTO;
import pl.lodz.p.model.MongoUUID;
import pl.lodz.p.model.user.Client;
import pl.lodz.p.model.user.Role;
import pl.lodz.p.model.user.Standard;
import pl.lodz.p.model.user.User;
import pl.lodz.p.security.JwsProvider;
import pl.lodz.p.security.interfaces.IJwsProvider;
import pl.lodz.p.service.IUserService;
import pl.lodz.p.service.implementation.UserService;

import java.util.*;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(MockitoExtension.class)
public class ClientControllerTests {
    private MockMvc mockMvc;

    @Mock
    private IUserService userService;

    @Mock
    private IJwsProvider jwsProvider;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    public void createUser_CREATED_Test() {
        BindingResult bindingResult = new BeanPropertyBindingResult(userController, "user");
        ResponseEntity<Object> response = userController.createUser(new Client("jan", "janowski", "janek", "janeczek", "janeczek@janko.jan", new Standard()), bindingResult);

        Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    public void createUser_BAD_REQUEST_Test() {
        BindingResult bindingResult = new BeanPropertyBindingResult(userController, "user");
        bindingResult.addError(new ObjectError("client", "test error"));
        ResponseEntity<Object> response = userController.createUser(new Client("jan", "janowski", "janek", "janeczek", "janeczek@janko.jan", new Standard()), bindingResult);

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void createUser_CONFLICT_Test() {
        BindingResult bindingResult = new BeanPropertyBindingResult(userController, "user");
        Client client = new Client("jan", "janowski", "janek", "janeczek", "janeczek@janko.jan", new Standard());

        when(userService.createUser(client)).thenReturn(client);
        ResponseEntity<Object> response = userController.createUser(client, bindingResult);
        Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());

        when(userService.createUser(client)).thenThrow(new RuntimeException());
        ResponseEntity<Object> response2 = userController.createUser(client, bindingResult);
        Assertions.assertEquals(HttpStatus.CONFLICT, response2.getStatusCode());

        verify(userService, times(2)).createUser(client);
    }


    @Test
    public void createUser_CONFLICT_2_Test() {
        BindingResult bindingResult = new BeanPropertyBindingResult(userController, "user");
        Client client = new Client("jan", "janowski", "janek", "janeczek", "janeczek@janko.jan", new Standard());

        ResponseEntity<Object> response = userController.createUser(client, null);
        Assertions.assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }

    @Test
    public void getAllUsers_OK_Test() {
        List<User> balls = new ArrayList<>();
        balls.add(new Client("jan", "janowski", "janek", "janeczek", "janeczek@janko.jan", new Standard()));
        when(userService.getAllUsers()).thenReturn(balls);
        ResponseEntity<Object> response = userController.getAllUsers();
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void getAllUsers_NOT_FOUND_Test() {
        when(userService.getAllUsers()).thenThrow(new RuntimeException());
        ResponseEntity<Object> response = userController.getAllUsers();
        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void getUser_OK() {
        UUID uuid = UUID.randomUUID();
        when(userService.getUser(uuid)).thenReturn(new Client(new MongoUUID(uuid), "jan", "janowski", "janek", "janeczek", "janeczek@janko.jan", Role.CLIENT, true,  new Standard(), 0));
        when(jwsProvider.generateJws(uuid.toString(), "janowski")).thenReturn("klucz");
        ResponseEntity<Object> response = userController.getUser(new UuidDTO(uuid.toString()));
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void getUser_BAD_REQUEST_Test() {
        UUID uuid = UUID.randomUUID();
        when(userService.getUser(uuid)).thenThrow(new RuntimeException());
        ResponseEntity<Object> response = userController.getUser(new UuidDTO(uuid.toString()));
        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void getUser_INTERNAL_SERVER_ERROR_Test() {
        UUID uuid = UUID.randomUUID();
        when(userService.getUser(uuid)).thenReturn(new Client(new MongoUUID(uuid), "jan", "janowski", "janek", "janeczek", "janeczek@janko.jan", Role.CLIENT, true,  new Standard(), 0));
        when(jwsProvider.generateJws(uuid.toString(), "janowski")).thenThrow(new RuntimeException());
        ResponseEntity<Object> response = userController.getUser(new UuidDTO(uuid.toString()));
        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void updateUser_NO_CONTENT_Test() {
        UUID uuid = UUID.randomUUID();
        Map<String, Object> fields = new HashMap<>();
        fields.put("firstname", "januszewicz");
        String ifmatchheader = "xdd";
        BindingResult bindingResult = new BeanPropertyBindingResult(fields, "fields");

        Client client = new Client(new MongoUUID(uuid), "jan", "janowski", "janek", "janeczek", "janeczek@janko.jan", Role.CLIENT, true,  new Standard(), 0);

        when(userService.getUser(uuid)).thenReturn(client);
        when(jwsProvider.validateJws(ifmatchheader, client.getEntityId().getUuid().toString(), client.getUsername())).thenReturn(true);
        doNothing().when(userService).updateUser(uuid, fields);

        ResponseEntity<Object> response = userController.updateUser(new UuidDTO(uuid.toString()), fields, ifmatchheader, bindingResult);

        Assertions.assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    public void updateUser_CONFLICT_Test() {
        UUID uuid = UUID.randomUUID();
        Map<String, Object> fields = new HashMap<>();
        fields.put("firstname", "januszewicz");
        String ifmatchheader = "xdd";
        BindingResult bindingResult = new BeanPropertyBindingResult(fields, "fields");

        Client client = new Client(new MongoUUID(uuid), "jan", "janowski", "janek", "janeczek", "janeczek@janko.jan", Role.CLIENT, true,  new Standard(), 0);

        when(userService.getUser(uuid)).thenReturn(client);
        when(jwsProvider.validateJws(ifmatchheader, client.getEntityId().getUuid().toString(), client.getUsername())).thenReturn(false);

        ResponseEntity<Object> response = userController.updateUser(new UuidDTO(uuid.toString()), fields, ifmatchheader, bindingResult);

        Assertions.assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }

    @Test
    public void updateUser_BAD_REQUEST_Test() {
        UUID uuid = UUID.randomUUID();
        Map<String, Object> fields = new HashMap<>();
        fields.put("firstname", "januszewicz");
        String ifmatchheader = "xdd";
        BindingResult bindingResult = new BeanPropertyBindingResult(fields, "fields");
        bindingResult.addError(new ObjectError("firstname", "test errror"));

        ResponseEntity<Object> response = userController.updateUser(new UuidDTO(uuid.toString()), fields, ifmatchheader, bindingResult);

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void updateUser_INTERNAL_SERVER_ERROR_Test() {
        UUID uuid = UUID.randomUUID();
        Map<String, Object> fields = new HashMap<>();
        fields.put("firstname", "januszewicz");
        String ifmatchheader = "xdd";
        BindingResult bindingResult = new BeanPropertyBindingResult(fields, "fields");

        Client client = new Client(new MongoUUID(uuid), "jan", "janowski", "janek", "janeczek", "janeczek@janko.jan", Role.CLIENT, true,  new Standard(), 0);

        when(userService.getUser(uuid)).thenReturn(client);
        when(jwsProvider.validateJws(ifmatchheader, client.getEntityId().getUuid().toString(), client.getUsername())).thenReturn(true);
        doThrow(new RuntimeException("xd")).when(userService).updateUser(uuid, fields);

        ResponseEntity<Object> response = userController.updateUser(new UuidDTO(uuid.toString()), fields, ifmatchheader, bindingResult);

        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }
}