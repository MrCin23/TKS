package pl.lodz.p.soap.model.user;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import pl.lodz.p.rest.model.user.RESTUser;

import java.util.Collection;
import java.util.Collections;

public class SOAPUserPrincipal implements UserDetails {
    private RESTUser RESTUser;

    public SOAPUserPrincipal(RESTUser RESTUser) {
        this.RESTUser = RESTUser;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority(RESTUser.getRESTRole().toString()));
    }

    @Override
    public String getPassword() {
        return RESTUser.getPassword();
    }

    @Override
    public String getUsername() {
        return RESTUser.getUsername();
    }
}