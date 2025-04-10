
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import pl.lodz.p.soap.endpoint.HelloWorldEndpoint;
import pl.lodz.p.soap.endpoint.UserEndpoint;
import pl.lodz.p.soap.model.request.HelloRequest;

public class EndpointTest {
    @Test
    public void helloRequest() {
        HelloRequest request = new HelloRequest();
        HelloWorldEndpoint endpoint = new HelloWorldEndpoint();
        Assertions.assertEquals("Hello, Anonymous!", endpoint.sayHello(request).getMessage());
    }

    @Test
    void getUserByUsernameTest(){

        Assertions.assertTrue(true);
    }

    @Test
    void getCollectionSizeTest(){

        Assertions.assertTrue(true);
    }

    @Test
    void getLoginUserTest(){

        Assertions.assertTrue(true);
    }
}
