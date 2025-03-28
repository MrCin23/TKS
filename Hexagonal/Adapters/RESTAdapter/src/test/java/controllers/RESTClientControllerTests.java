package controllers;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import pl.lodz.p.rest.controller.UserController;
import pl.lodz.p.core.domain.user.Client;
import pl.lodz.p.core.domain.user.Standard;
import pl.lodz.p.core.domain.user.User;
import pl.lodz.p.rest.model.user.RESTClient;
import pl.lodz.p.rest.model.user.RESTStandard;
import pl.lodz.p.rest.model.user.RESTUser;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class RESTClientControllerTests {
    private MockMvc mockMvc;

//    @Mock
//    private IUserService userService; //todo zmienic na port

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

//        when(userService.createUser(client)).thenReturn(client);
        ResponseEntity<Object> response = userController.createUser(client, bindingResult);
        Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());

//        when(userService.createUser(client)).thenThrow(new RuntimeException());
        ResponseEntity<Object> response2 = userController.createUser(client, bindingResult);
        Assertions.assertEquals(HttpStatus.CONFLICT, response2.getStatusCode());

//        verify(userService, times(2)).createUser(client);
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
//        when(userService.getAllUsers()).thenReturn(a);
        ResponseEntity<Object> response = userController.getAllUsers();
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void getAllUsers_NOT_FOUND_Test() {
//        when(userService.getAllUsers()).thenThrow(new RuntimeException());
        ResponseEntity<Object> response = userController.getAllUsers();
        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}