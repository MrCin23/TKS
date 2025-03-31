package pl.lodz.p.soap.endpoint;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;
import pl.lodz.p.soap.model.request.GetUserByUsernameRequest;
import pl.lodz.p.soap.model.user.SOAPUser;
import pl.lodz.p.ui.SOAPUserServicePort;

@Endpoint
@AllArgsConstructor
public class UserEndpoint {
    private static final String NAMESPACE_URI = "http://example.com/users";
    @Qualifier("SOAPUserServicePort")
    private SOAPUserServicePort userService;

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getUserByUsername")
    @ResponsePayload
    public SOAPUser getUserByUsername(@RequestPayload GetUserByUsernameRequest request) {
        System.out.println("Recieved" + request.getUsername());
        return userService.getUserByUsername(request.getUsername());
    }
}
