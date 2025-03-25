package pl.lodz.p.soap;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import io.spring.guides.gs_producing_web_service.GetUserInfo;
import io.spring.guides.gs_producing_web_service.GetUserResponse;
import io.spring.guides.gs_producing_web_service.User;
import pl.lodz.p.infrastructure.user.UGet;

@NoArgsConstructor
@AllArgsConstructor
@Endpoint
public class UserEndpoint {
    private static final String NAMESPACE_URI = "http://spring.io/guides/gs-producing-web-service";

    private UGet uGet;

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getUserInfo")
    @ResponsePayload
    public GetUserResponse getUserInfo(@RequestPayload GetUserInfo request) {
        GetUserResponse response = new GetUserResponse();
        User user = new User();
        user.setFirstname(uGet.getUserByUsername(request.getUsername()).getFirstName());
        user.setSurname(uGet.getUserByUsername(request.getUsername()).getSurname());
        user.setEmailAddress(uGet.getUserByUsername(request.getUsername()).getEmailAddress());
        response.setUser(user);
        return response;
    }
}