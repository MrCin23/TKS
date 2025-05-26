package pl.lodz.p.core.domain.user;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import pl.lodz.p.core.domain.AbstractEntityMgd;
import pl.lodz.p.core.domain.MongoUUID;


import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.UUID;


@Setter
@Getter
@NoArgsConstructor
public class Client extends AbstractEntityMgd implements UserDetails {
    private String username;
    private ClientType clientType;
    private int currentRents;
    private boolean active;

    @Override
    public String toString() {
        return super.toString() + "::Client{" +
                "clientType=" + clientType +
                ", currentRents=" + currentRents +
                '}';
    }

    public Client(MongoUUID entityId, String username, ClientType clientType, int currentRents, boolean active) {
        super(entityId);
        this.username = username;
        this.clientType = clientType;
        this.currentRents = currentRents;
        this.active = active;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_CLIENT"));
    }

    @Override
    public String getPassword() {
        return "[protected]";
    }
}
