package pl.lodz.p.soap.model.request;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Setter;

@Setter
@XmlRootElement(name = "GetUserByUsernameRequest", namespace = "http://example.com/users")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class GetUserByUsernameRequest {
    private String username;

    @XmlElement(namespace = "http://example.com/users")
    public String getUsername() {
        return username;
    }
}
