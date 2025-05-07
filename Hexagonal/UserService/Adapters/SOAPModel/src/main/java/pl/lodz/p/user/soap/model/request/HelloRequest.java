package pl.lodz.p.user.soap.model.request;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;


import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Setter;

@Setter
@XmlRootElement(name = "HelloRequest", namespace = "http://example.com/hello")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class HelloRequest {
    private String name;

    @XmlElement(namespace = "http://example.com/hello")
    public String getName() {
        return name;
    }

}