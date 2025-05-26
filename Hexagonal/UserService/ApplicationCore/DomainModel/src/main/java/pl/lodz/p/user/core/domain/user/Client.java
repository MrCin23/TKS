package pl.lodz.p.user.core.domain.user;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import pl.lodz.p.user.core.domain.MongoUUID;


import java.util.Collection;
import java.util.List;
import java.util.UUID;


@Setter
@Getter
@NoArgsConstructor
public class Client extends User{
    private ClientType clientType;
    private int currentRents;

    @Override
    public String toString() {
        return super.toString() + "::Client{" +
                "clientType=" + clientType +
                ", currentRents=" + currentRents +
                '}';
    }

    public Client(String firstName,
                  String surname,
                  String username,
                  String password,
                  String emailAddress,
                  ClientType clientType) {
        super(new MongoUUID(UUID.randomUUID()), firstName, username, password, surname, emailAddress, Role.CLIENT, true);
        this.clientType = clientType;
        this.currentRents = 0;
    }

    public Client(MongoUUID userId,
                  String firstName,
                  String username,
                  String password,
                  String surname,
                  String emailAddress,
                  Role role,
                  boolean active,
                  ClientType clientType,
                  int currentRents) {
        super(userId, firstName, username, password, surname, emailAddress, role, active);
        this.clientType = clientType;
        this.currentRents = currentRents;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }
}
