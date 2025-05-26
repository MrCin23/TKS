package pl.lodz.p.user.core.domain.user;

import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import pl.lodz.p.user.core.domain.MongoUUID;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@NoArgsConstructor
public class Admin extends User{
    public Admin(String firstName, String surname, String username, String password, String emailAddress) {
        super(new MongoUUID(UUID.randomUUID()), firstName, username, password, surname, emailAddress, Role.ADMIN, true);
    }

    public Admin(MongoUUID uuid, String firstName, String surname, String username, String emailAddress, String password) {
        super(uuid, firstName, username, password, surname, emailAddress, Role.ADMIN, true);
    }

    @Override
    public String toString() {
        return super.toString() + "::Admin{}";
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }
}
