package pl.lodz.p.soap.model;


import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import pl.lodz.p.soap.model.user.SOAPUser;

@XmlRootElement(name = "GetUserByUsernameResponse", namespace = "http://example.com/users")
public class GetUserByUsernameResponse {

    private SOAPUser user;

    @XmlElement
    public SOAPUser getUser() {
        return user;
    }

    public void setUser(SOAPUser user) {
        this.user = user;
    }
}
