package controllers;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import pl.lodz.p.core.services.security.JwsProvider;
import pl.lodz.p.rest.controller.ClientController;
import pl.lodz.p.rest.model.RESTMongoUUID;
import pl.lodz.p.rest.model.dto.ChangePasswordDTO;
import pl.lodz.p.rest.model.dto.LoginDTO;
import pl.lodz.p.rest.model.dto.UuidDTO;
import pl.lodz.p.rest.model.user.RESTClient;
import pl.lodz.p.rest.model.user.RESTRole;
import pl.lodz.p.rest.model.user.RESTStandard;
import pl.lodz.p.ui.RESTClientServicePort;

import java.util.*;

import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class RESTClientControllerTests {
    private MockMvc mockMvc;
    UUID uuid = UUID.fromString("00000000-0000-0000-0000-000000000000");
    UuidDTO dto = new UuidDTO(uuid.toString());
    RESTClient client = new RESTClient(new RESTMongoUUID(uuid), "janek", true, new RESTStandard(), 0);

    @Mock
    private RESTClientServicePort userServicePort;

    @Mock
    private JwsProvider jwsProvider;

    @InjectMocks
    private ClientController userController;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    public void createClient_CREATED_Test() {
        BindingResult bindingResult = new BeanPropertyBindingResult(userController, "user");
        ResponseEntity<Object> response = userController.createClient(new RESTClient("janek", new RESTStandard(),true), bindingResult);

        Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    public void createClient_BAD_REQUEST_Test() {
        BindingResult bindingResult = new BeanPropertyBindingResult(userController, "user");
        bindingResult.addError(new ObjectError("client", "test error"));
        ResponseEntity<Object> response = userController.createClient(new RESTClient("janek", new RESTStandard(),true), bindingResult);

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void createClient_CONFLICT_Test() {
        BindingResult bindingResult = new BeanPropertyBindingResult(userController, "user");
        RESTClient client = new RESTClient("janek", new RESTStandard(),true);

        when(userServicePort.createClient(client)).thenReturn(client);
        ResponseEntity<Object> response = userController.createClient(client, bindingResult);
        Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());

        when(userServicePort.createClient(client)).thenThrow(new RuntimeException());
        ResponseEntity<Object> response2 = userController.createClient(client, bindingResult);
        Assertions.assertEquals(HttpStatus.CONFLICT, response2.getStatusCode());

        verify(userServicePort, times(2)).createClient(client);
    }

    @Test
    public void getAllClients_NOT_FOUND_Test() {
        when(userServicePort.getAllClients()).thenThrow(new RuntimeException());
        ResponseEntity<Object> response = userController.getAllClients();
        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }


    @Test
    public void getClient_NOT_FOUND_Test() {
        when(userServicePort.getClient(uuid)).thenThrow(new RuntimeException());
        ResponseEntity<Object> response = userController.getClient(dto);
        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void getClient_INTERNAL_SERVER_ERROR_Test() {
        when(userServicePort.getClient(uuid)).thenReturn(client);
        when(jwsProvider.generateJws(dto.getUuid(), "janowski")).thenThrow(new RuntimeException());
        ResponseEntity<Object> response = userController.getClient(dto);
        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void findClient_OK_Test() {
        when(userServicePort.getClientByUsername("janowski")).thenReturn(client);
        ResponseEntity<Object> response = userController.findClient("janowski");
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void findClient_NOT_FOUND_Test() {
        when(userServicePort.getClientByUsername("janowski")).thenThrow(new RuntimeException());
        ResponseEntity<Object> response = userController.findClient("janowski");
        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        Assertions.assertEquals(response.getBody(), "No users matching");
    }

    @Test
    public void findClient_INTERNAL_SERVER_ERROR_Test() {
        when(userServicePort.getClientByUsername("janowski")).thenReturn(client);
        when(jwsProvider.generateJws(client.getEntityId().getUuid().toString(), client.getUsername())).thenThrow(new RuntimeException());
        ResponseEntity<Object> response = userController.findClient("janowski");
        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void findClients_OK_Test() {
        List<RESTClient> users = new ArrayList<>();
        users.add(client);
        when(userServicePort.getClientsByUsername("janowski")).thenReturn(users);
        ResponseEntity<Object> response = userController.findClients("janowski");
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void findClients_NOT_FOUND_Test() {
        when(userServicePort.getClientsByUsername("janowski")).thenThrow(new RuntimeException());
        ResponseEntity<Object> response = userController.findClients("janowski");
        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        Assertions.assertEquals(response.getBody(), "No users matching");
    }
}