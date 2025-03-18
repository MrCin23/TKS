package pl.lodz.p.repo.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonDiscriminator;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.springframework.security.crypto.bcrypt.BCrypt;
import pl.lodz.p.repo.AbstractEnt;
import pl.lodz.p.repo.MongoUUIDEnt;

@Getter
@Setter
@NoArgsConstructor
@BsonDiscriminator(value="User", key="_clazz")
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "_clazz"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = ClientEnt.class, name = "Client"),
        @JsonSubTypes.Type(value = AdminEnt.class, name = "Admin"),
        @JsonSubTypes.Type(value = ResourceManagerEnt.class, name = "ResourceManager")
})
public abstract class UserEnt extends AbstractEnt {
    @BsonProperty("firstName")
    @NotBlank(message = "First name cannot be blank")
    @Size(min = 2, max = 32)
    private String firstName;
    @BsonProperty("surname")
    @NotBlank(message = "First name cannot be blank")
    @Size(min = 2, max = 32)
    private String surname;
    @NotBlank(message = "Username cannot be blank")
    @Size(min = 4, max = 20)
    @BsonProperty("username")
    private String username;
    @BsonProperty("password")
    @NotBlank(message = "Password cannot be blank")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
    @BsonProperty("emailAddress")
    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Email address has to be valid")
    private String emailAddress;
    @BsonProperty("role")
    private RoleEnt roleEnt;
    @BsonProperty("active")
    private boolean active;


    @BsonCreator
    public UserEnt(@BsonProperty("_id") MongoUUIDEnt userId,
                   @BsonProperty("firstName") String firstName,
                   @BsonProperty("username") String username,
                   @BsonProperty("password") String password,
                   @BsonProperty("surname") String surname,
                   @BsonProperty("emailAddress") String emailAddress,
                   @BsonProperty("role") RoleEnt roleEnt,
                   @BsonProperty("active") boolean active) {
        super(userId);
        this.firstName = firstName;
        this.surname = surname;
        this.username = username;
        this.password = BCrypt.hashpw(password, BCrypt.gensalt());
        this.emailAddress = emailAddress;
        this.roleEnt = roleEnt;
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
                ", role=" + roleEnt +
                ", active=" + active +
                '}';
    }
}
