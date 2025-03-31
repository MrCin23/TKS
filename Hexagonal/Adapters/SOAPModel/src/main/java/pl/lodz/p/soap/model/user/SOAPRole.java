package pl.lodz.p.soap.model.user;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlType;

@XmlType(name = "RoleType", namespace = "http://example.com/users")
@XmlEnum
public enum SOAPRole {
    @XmlEnumValue("ADMIN") ADMIN,
    @XmlEnumValue("RESOURCE_MANAGER") RESOURCE_MANAGER,
    @XmlEnumValue("CLIENT") CLIENT
}
