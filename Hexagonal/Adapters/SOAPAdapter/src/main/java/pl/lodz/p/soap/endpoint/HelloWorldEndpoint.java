package pl.lodz.p.soap.endpoint;

import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;
import pl.lodz.p.soap.model.request.HelloRequest;
import pl.lodz.p.soap.model.HelloResponse;

@Endpoint
public class HelloWorldEndpoint {
    private static final String NAMESPACE_URI = "http://example.com/hello";

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "HelloRequest")
    @ResponsePayload
    public HelloResponse sayHello(@RequestPayload HelloRequest request) {
        HelloResponse response = new HelloResponse();
        response.setMessage("Hello, " + (request.getName() != null ? request.getName() : "Anonymous") + "!");
        return response;
    }
}