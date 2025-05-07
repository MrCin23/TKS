
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import pl.lodz.p.user.soap.endpoint.HelloWorldEndpoint;
import pl.lodz.p.user.soap.endpoint.UserEndpoint;
import pl.lodz.p.user.soap.model.request.GetUserByUsernameRequest;
import pl.lodz.p.user.soap.model.request.HelloRequest;
import pl.lodz.p.user.ui.SOAPUserServicePort;
import pl.lodz.p.users.*;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserEndpointTest {
    private MockMvc mockMvc;
    private static final UserType userType = new UserType();


    @Mock
    private SOAPUserServicePort userServicePort;

    @InjectMocks
    private UserEndpoint userEndpoint;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(userEndpoint).build();
    }

    @BeforeAll
    public static void setupClass() {
        userType.setFirstName("jan");
        userType.setSurname("jan");
        userType.setActive(true);
        userType.setUsername("jann");
        userType.setEmailAddress("jan@jan.jan");
    }

    @Test
    public void helloRequest() {
        HelloRequest request = new HelloRequest();
        HelloWorldEndpoint endpoint = new HelloWorldEndpoint();
        Assertions.assertEquals("Hello, Anonymous!", endpoint.sayHello(request).getMessage());

        HelloRequest request2 = new HelloRequest();
        request2.setName("A");
        Assertions.assertEquals("Hello, A!", endpoint.sayHello(request2).getMessage());
    }

    @Test
    void getUserByUsernameTest(){
        GetUserByUsernameRequest request = new GetUserByUsernameRequest();
        request.setUsername("jan");
        when(userServicePort.getUserByUsername("jan")).thenReturn(userType);
        GetUserByUsernameResponse response = userEndpoint.getUserByUsername(request);
        Assertions.assertEquals(userType.toString(), response.getUser().toString());
    }

    @Test
    void getCollectionSizeTest() {
        CollectionSize request = new CollectionSize();
        Size size = new Size();
        size.setSize(42);

        when(userServicePort.size()).thenReturn(42L);

        Size response = userEndpoint.getCollectionSize(request);

        Assertions.assertEquals(42, response.getSize());
    }


    @Test
    void getLoginUserTest() {
        LoginUser request = new LoginUser();
        request.setUsername("jan");
        request.setPassword("password");

        when(userServicePort.getUserByUsername(request)).thenReturn("mockToken123");

        Token token = userEndpoint.getLoginUser(request);

        Assertions.assertEquals("mockToken123", token.getToken());
    }

}
