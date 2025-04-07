package pl.lodz.p.soap.endpoint;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;
import pl.lodz.p.soap.model.GetUserByUsernameResponse;
import pl.lodz.p.soap.model.request.GetUserByUsernameRequest;
import pl.lodz.p.soap.model.user.SOAPUser;
import pl.lodz.p.ui.SOAPUserServicePort;

@Endpoint
public class UserEndpoint {
    private static final String NAMESPACE_URI = "http://p.lodz.pl/users";
    private final SOAPUserServicePort userServicePort;

    public UserEndpoint(SOAPUserServicePort userServicePort) {
        this.userServicePort = userServicePort;
    }
    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "GetUserByUsernameRequest")
    @ResponsePayload
    public GetUserByUsernameResponse getUserByUsername(@RequestPayload GetUserByUsernameRequest request) {
        System.out.println("Received" + request.getUsername());
        GetUserByUsernameResponse response = new GetUserByUsernameResponse();
        response.setUser(userServicePort.getUserByUsername(request.getUsername()));
        return response;
//        return null;
    }
}
