package pl.lodz.p.soap.endpoint;

import lombok.AllArgsConstructor;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;
import pl.lodz.p.soap.model.request.GetUserByUsernameRequest;
import pl.lodz.p.soap.model.user.SOAPUser;
import pl.lodz.p.ui.UserServicePort;

@Endpoint
@AllArgsConstructor
public class UserEndpoint {
    private static final String NAMESPACE_URI = "http://example.com/users";
    private UserServicePort userService;

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getUserByUsername")
    @ResponsePayload
    public SOAPUser getUserByUsername(@RequestPayload GetUserByUsernameRequest request) {
        System.out.println(request.getUsername());
        SOAPUser user = userService.getUserByUsername(request.getUsername());
    }
}
