package pl.lodz.p.repo.user;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

public class UserPrincipalEnt implements UserDetails {
    private UserEnt userEnt;

    public UserPrincipalEnt(UserEnt userEnt) {
        this.userEnt = userEnt;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority(userEnt.getRoleEnt().toString()));
    }

    @Override
    public String getPassword() {
        return userEnt.getPassword();
    }

    @Override
    public String getUsername() {
        return userEnt.getUsername();
    }
}