package pl.lodz.p.model.user;

import com.webauthn4j.util.exception.NotImplementedException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class UserPrincipal implements UserDetails {
    private User user;

    public UserPrincipal(User user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority(user.getRole().toString()));
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
        //TODO
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
        //TODO
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
        //TODO
    }

    @Override
    public boolean isEnabled() {
        return true;
        //TODO
    }
}
