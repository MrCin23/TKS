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
import pl.lodz.p.user.core.domain.exception.DeactivatedUserException;
import pl.lodz.p.user.core.domain.exception.WrongPasswordException;
//import pl.lodz.p.user.core.services.security.JwsProvider;
import pl.lodz.p.user.rest.controller.JwsProvider;
import pl.lodz.p.user.rest.controller.UserController;
import pl.lodz.p.user.rest.model.RESTMongoUUID;
import pl.lodz.p.user.rest.model.dto.ChangePasswordDTO;
import pl.lodz.p.user.rest.model.dto.LoginDTO;
import pl.lodz.p.user.rest.model.dto.UuidDTO;
import pl.lodz.p.user.rest.model.user.RESTClient;
import pl.lodz.p.user.rest.model.user.RESTRole;
import pl.lodz.p.user.rest.model.user.RESTStandard;
import pl.lodz.p.user.rest.model.user.RESTUser;
import pl.lodz.p.user.ui.RESTUserServicePort;

import java.util.*;

import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class RESTClientControllerTests {
    private MockMvc mockMvc;
    UUID uuid = UUID.fromString("00000000-0000-0000-0000-000000000000");
    UuidDTO dto = new UuidDTO(uuid.toString());
    RESTUser client = new RESTClient(new RESTMongoUUID(uuid), "jan", "janowski", "janek", "janeczek", "janeczek@janko.jan", RESTRole.CLIENT, true, new RESTStandard(), 0);

    @Mock
    private RESTUserServicePort userServicePort;

    @Mock
    private JwsProvider jwsProvider;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    public void createUser_CREATED_Test() {
        BindingResult bindingResult = new BeanPropertyBindingResult(userController, "user");
        ResponseEntity<Object> response = userController.createUser(new RESTClient("jan", "janowski", "janek", "janeczek", "janeczek@janko.jan", new RESTStandard()), bindingResult);

        Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    public void createUser_BAD_REQUEST_Test() {
        BindingResult bindingResult = new BeanPropertyBindingResult(userController, "user");
        bindingResult.addError(new ObjectError("client", "test error"));
        ResponseEntity<Object> response = userController.createUser(new RESTClient("jan", "janowski", "janek", "janeczek", "janeczek@janko.jan", new RESTStandard()), bindingResult);

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void createUser_CONFLICT_Test() {
        BindingResult bindingResult = new BeanPropertyBindingResult(userController, "user");
        RESTClient client = new RESTClient("jan", "janowski", "janek", "janeczek", "janeczek@janko.jan", new RESTStandard());

        when(userServicePort.createUser(client)).thenReturn(client);
        ResponseEntity<Object> response = userController.createUser(client, bindingResult);
        Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());

        when(userServicePort.createUser(client)).thenThrow(new RuntimeException());
        ResponseEntity<Object> response2 = userController.createUser(client, bindingResult);
        Assertions.assertEquals(HttpStatus.CONFLICT, response2.getStatusCode());

        verify(userServicePort, times(2)).createUser(client);
    }


    @Test
    public void createUser_CONFLICT_2_Test() {
        BindingResult bindingResult = new BeanPropertyBindingResult(userController, "user");
        RESTClient client = new RESTClient("jan", "janowski", "janek", "janeczek", "janeczek@janko.jan", new RESTStandard());

        ResponseEntity<Object> response = userController.createUser(client, null);
        Assertions.assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }

    @Test
    public void getAllUsers_OK_Test() {
        List<RESTUser> a = new ArrayList<>();
        a.add(new RESTClient("jan", "janowski", "janek", "janeczek", "janeczek@janko.jan", new RESTStandard()));
        when(userServicePort.getAllUsers()).thenReturn(a);
        ResponseEntity<Object> response = userController.getAllUsers();
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void getAllUsers_NOT_FOUND_Test() {
        when(userServicePort.getAllUsers()).thenThrow(new RuntimeException());
        ResponseEntity<Object> response = userController.getAllUsers();
        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void getUser_OK_Test() {
        when(userServicePort.getUser(uuid)).thenReturn(client);
        when(jwsProvider.generateJws(dto.getUuid(), "janowski")).thenReturn("jan");
        ResponseEntity<Object> response = userController.getUser(dto);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void getUser_NOT_FOUND_Test() {
        when(userServicePort.getUser(uuid)).thenThrow(new RuntimeException());
        ResponseEntity<Object> response = userController.getUser(dto);
        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void getUser_INTERNAL_SERVER_ERROR_Test() {
        when(userServicePort.getUser(uuid)).thenReturn(client);
        when(jwsProvider.generateJws(dto.getUuid(), "janowski")).thenThrow(new RuntimeException());
        ResponseEntity<Object> response = userController.getUser(dto);
        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void updateUser_NO_CONTENT_Test() {
        Map<String, Object> map = new HashMap<>();
        BindingResult bindingResult = new BeanPropertyBindingResult(userController, "user");
        map.put("firstName", "Jan");
        when(userServicePort.getUser(uuid)).thenReturn(client);
        when(jwsProvider.validateJws("If-Match", client.getEntityId().getUuid().toString(), client.getUsername())).thenReturn(true);
        doNothing().when(userServicePort).updateUser(uuid, map);
        ResponseEntity<Object> response = userController.updateUser(dto, map, "If-Match", bindingResult);
        verify(userServicePort, times(1)).updateUser(uuid, map);
        Assertions.assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    public void updateUser_BAD_REQUEST_Test() {
        Map<String, Object> map = new HashMap<>();
        BindingResult bindingResult = new BeanPropertyBindingResult(userController, "user");
        bindingResult.addError(new ObjectError("client", "test error"));
        ResponseEntity<Object> response = userController.updateUser(dto, map, "If-Match", bindingResult);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void updateUser_nullIfMatch_CONFLICT_Test() {
        BindingResult bindingResult = new BeanPropertyBindingResult(userController, "user");
        Map<String, Object> map = new HashMap<>();
        when(userServicePort.getUser(uuid)).thenReturn(client);
        ResponseEntity<Object> response = userController.updateUser(dto, map, null, bindingResult);
        Assertions.assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }

    @Test
    public void updateUser_invalidJws_CONFLICT_Test() {
        BindingResult bindingResult = new BeanPropertyBindingResult(userController, "user");
        Map<String, Object> map = new HashMap<>();
        when(userServicePort.getUser(uuid)).thenReturn(client);
        when(jwsProvider.validateJws("If-Match", client.getEntityId().getUuid().toString(), client.getUsername())).thenReturn(false);
        ResponseEntity<Object> response = userController.updateUser(dto, map, "If-Match", bindingResult);
        Assertions.assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }

    @Test
    public void updateUser_INTERNAL_SERVER_ERROR_Test() {
        Map<String, Object> map = new HashMap<>();
        BindingResult bindingResult = new BeanPropertyBindingResult(userController, "user");
        map.put("firstName", "Jan");
        when(userServicePort.getUser(uuid)).thenReturn(client);
        when(jwsProvider.validateJws("If-Match", client.getEntityId().getUuid().toString(), client.getUsername())).thenReturn(true);
        doThrow(new RuntimeException()).when(userServicePort).updateUser(uuid, map);
        ResponseEntity<Object> response = userController.updateUser(dto, map, "If-Match", bindingResult);
        verify(userServicePort, times(1)).updateUser(uuid, map);
        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void deactivateUser_NO_CONTENT_Test() {
        doNothing().when(userServicePort).deactivateUser(uuid);
        ResponseEntity<Object> response = userController.deactivateUser(dto);
        Assertions.assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    public void deactivateUser_BAD_REQUEST_Test() {
        doThrow(new RuntimeException()).when(userServicePort).deactivateUser(uuid);
        ResponseEntity<Object> response = userController.deactivateUser(dto);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void activateUser_NO_CONTENT_Test() {
        doNothing().when(userServicePort).activateUser(uuid);
        ResponseEntity<Object> response = userController.activateUser(dto);
        Assertions.assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    public void activateUser_BAD_REQUEST_Test() {
        doThrow(new RuntimeException()).when(userServicePort).activateUser(uuid);
        ResponseEntity<Object> response = userController.activateUser(dto);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    /**
     * Tutaj testy nazwyają się login od endpointów RESTowych.
     * Metoda w rzeczywistości nazywa się findUser, co jest nieścisłością.
     */
    @Test
    public void login_OK_Test() {
        LoginDTO dto = new LoginDTO("janowski", "janek");
        when(userServicePort.getUserByUsername(dto)).thenReturn("Żeton");
        ResponseEntity<Object> response = userController.findUser(dto);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(response.getBody(), "Żeton");
    }

    @Test
    public void login_deactivatedUser_FORBIDDEN_Test() {
        LoginDTO dto = new LoginDTO("janowski", "janek");
        when(userServicePort.getUserByUsername(dto)).thenThrow(new DeactivatedUserException("test"));
        ResponseEntity<Object> response = userController.findUser(dto);
        Assertions.assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        Assertions.assertEquals(response.getBody(), "User is deactivated");
    }

    @Test
    public void login_wrongPassword_FORBIDDEN_Test() {
        LoginDTO dto = new LoginDTO("janowski", "janek");
        when(userServicePort.getUserByUsername(dto)).thenThrow(new WrongPasswordException("test"));
        ResponseEntity<Object> response = userController.findUser(dto);
        Assertions.assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }

    @Test
    public void login_NOT_FOUND_Test() {
        LoginDTO dto = new LoginDTO("janowski", "janek");
        when(userServicePort.getUserByUsername(dto)).thenThrow(new RuntimeException());
        ResponseEntity<Object> response = userController.findUser(dto);
        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        Assertions.assertEquals(response.getBody(), "No users found");
    }

    @Test
    public void findUser_OK_Test() {
        when(userServicePort.getUserByUsername("janowski")).thenReturn(client);
        ResponseEntity<Object> response = userController.findUser("janowski");
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void findUser_NOT_FOUND_Test() {
        when(userServicePort.getUserByUsername("janowski")).thenThrow(new RuntimeException());
        ResponseEntity<Object> response = userController.findUser("janowski");
        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        Assertions.assertEquals(response.getBody(), "No users matching");
    }

    @Test
    public void findUser_INTERNAL_SERVER_ERROR_Test() {
        when(userServicePort.getUserByUsername("janowski")).thenReturn(client);
        when(jwsProvider.generateJws(client.getEntityId().getUuid().toString(), client.getUsername())).thenThrow(new RuntimeException());
        ResponseEntity<Object> response = userController.findUser("janowski");
        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void findUsers_OK_Test() {
        List<RESTUser> users = new ArrayList<>();
        users.add(client);
        when(userServicePort.getUsersByUsername("janowski")).thenReturn(users);
        ResponseEntity<Object> response = userController.findUsers("janowski");
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void findUsers_NOT_FOUND_Test() {
        when(userServicePort.getUsersByUsername("janowski")).thenThrow(new RuntimeException());
        ResponseEntity<Object> response = userController.findUsers("janowski");
        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        Assertions.assertEquals(response.getBody(), "No users matching");
    }

    @Test
    public void logout_OK_Test() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeader("Authorization")).thenReturn("Bearer validToken");
        ResponseEntity<Object> response = userController.logout(request);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void logout_nullableToken_UNAUTHORIZED_Test() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeader("Authorization")).thenReturn(null);
        ResponseEntity<Object> response = userController.logout(request);
        Assertions.assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    public void logout_invalidToken_UNAUTHORIZED_Test() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeader("Authorization")).thenReturn("invalidToken");
        ResponseEntity<Object> response = userController.logout(request);
        Assertions.assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    public void changePassword_OK_Test() {
        ChangePasswordDTO dto = new ChangePasswordDTO("janowski", "janek", "janeczek");
        when(userServicePort.getUserByUsername(any(LoginDTO.class))).thenReturn("Żeton");
        doNothing().when(userServicePort).changePassword(dto.getUsername(), dto.getNewPassword());
        ResponseEntity<Object> response = userController.changePassword(dto);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void changePassword_FORBIDDEN_Test() {
        ChangePasswordDTO dto = new ChangePasswordDTO("janowski", "janek", "janeczek");
        when(userServicePort.getUserByUsername(any(LoginDTO.class))).thenThrow(new WrongPasswordException("test"));
        ResponseEntity<Object> response = userController.changePassword(dto);
        Assertions.assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }

    @Test
    public void changePassword_NOT_FOUND_Test() {
        ChangePasswordDTO dto = new ChangePasswordDTO("janowski", "janek", "janeczek");
        when(userServicePort.getUserByUsername(any(LoginDTO.class))).thenThrow(new RuntimeException());
        ResponseEntity<Object> response = userController.changePassword(dto);
        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}