package pl.lodz.p.soap;

import lombok.Setter;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@Setter
@XmlRootElement(name = "HelloRequest", namespace = "http://example.com/hello")
public class HelloRequest {
    private String name;

    @XmlElement
    public String getName() {
        return name;
    }

}
