package pl.lodz.p.soap.model.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.crypto.bcrypt.BCrypt;
import pl.lodz.p.rest.model.RESTAbstractEntityMgd;
import pl.lodz.p.rest.model.RESTMongoUUID;
import pl.lodz.p.rest.model.user.RESTAdmin;
import pl.lodz.p.rest.model.user.RESTClient;
import pl.lodz.p.rest.model.user.RESTResourceManager;
import pl.lodz.p.rest.model.user.RESTRole;

@Getter
@Setter
@NoArgsConstructor
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "_clazz"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = RESTClient.class, name = "Client"),
        @JsonSubTypes.Type(value = RESTAdmin.class, name = "Admin"),
        @JsonSubTypes.Type(value = RESTResourceManager.class, name = "ResourceManager")
})
public abstract class SOAPUser extends RESTAbstractEntityMgd {
    private String firstName;
    private String surname;
    private String username;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
    private String emailAddress;
    private RESTRole RESTRole;
    private boolean active;

    public SOAPUser(RESTMongoUUID userId,
                    String firstName,
                    String username,
                    String password,
                    String surname,
                    String emailAddress,
                    RESTRole RESTRole,
                    boolean active) {
        super(userId);
        this.firstName = firstName;
        this.surname = surname;
        this.username = username;
        this.password = password;//BCrypt.hashpw(password, BCrypt.gensalt());
        this.emailAddress = emailAddress;
        this.RESTRole = RESTRole;
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
                ", role=" + RESTRole +
                ", active=" + active +
                '}';
    }
}
