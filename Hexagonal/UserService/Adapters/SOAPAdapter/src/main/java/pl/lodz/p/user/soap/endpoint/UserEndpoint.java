package pl.lodz.p.user.soap.endpoint;

import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;
import pl.lodz.p.user.soap.model.request.GetUserByUsernameRequest;
import pl.lodz.p.user.ui.SOAPUserServicePort;
import pl.lodz.p.users.*;

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
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "collectionSize")
    @ResponsePayload
    public Size getCollectionSize(@RequestPayload CollectionSize request) {
        Size size = new Size();
        size.setSize(userServicePort.size());
        return size;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "loginUser")
    @ResponsePayload
    public Token getLoginUser(@RequestPayload LoginUser request) {
        Token token = new Token();
        token.setToken(userServicePort.getUserByUsername(request));
        return token;
    }
}
