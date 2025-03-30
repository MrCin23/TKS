package pl.lodz.p.soap;

import lombok.Setter;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@Setter
@XmlRootElement(name = "HelloResponse", namespace = "http://example.com/hello")
public class HelloResponse {
    private String message;

    @XmlElement
    public String getMessage() {
        return message;
    }

}
