package pl.lodz.p.soap.model.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.crypto.bcrypt.BCrypt;
import pl.lodz.p.soap.model.SOAPAbstractEntityMgd;
import pl.lodz.p.soap.model.SOAPMongoUUID;

@Getter
@Setter
@NoArgsConstructor
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "_clazz"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = SOAPClient.class, name = "Client"),
        @JsonSubTypes.Type(value = SOAPAdmin.class, name = "Admin"),
        @JsonSubTypes.Type(value = SOAPResourceManager.class, name = "ResourceManager")
})
public abstract class SOAPUser extends SOAPAbstractEntityMgd {
    private String firstName;
    private String surname;
    private String username;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
    private String emailAddress;
    private SOAPRole role;
    private boolean active;

    public SOAPUser(SOAPMongoUUID userId,
                    String firstName,
                    String username,
                    String password,
                    String surname,
                    String emailAddress,
                    SOAPRole role,
                    boolean active) {
        super(userId);
        this.firstName = firstName;
        this.surname = surname;
        this.username = username;
        this.password = password;//BCrypt.hashpw(password, BCrypt.gensalt());
        this.emailAddress = emailAddress;
        this.role = role;
        this.active = active;
    }

    public boolean checkPassword(String password) {
        return BCrypt.checkpw(password, this.password);
    }


    @Override
    public String toString() {
        return "User{" +
                "firstName='" + firstName + '\'' +
                ", surname='" + surname + '\'' +
                ", username='" + username + '\'' +
                ", emailAddress='" + emailAddress + '\'' +
                ", role=" + role +
                ", active=" + active +
                '}';
    }
}
