package ch.diedreifragezeichen.exama.userAdministration;

import java.util.Collection;
import java.util.Set;
import java.util.List;
import java.util.ArrayList;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@SuppressWarnings("serial")
public class UserDetailsExama implements UserDetails {

    private User user;

    public UserDetailsExama(User user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<Role> userRoles = user.getRoles();
        List<SimpleGrantedAuthority> userAuthorities = new ArrayList<>();
        for (Role role : userRoles) {
            userAuthorities.add(new SimpleGrantedAuthority(role.getName()));
        }
        return userAuthorities;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return user.isEnabled();
    }

    public String getFullName() {
        return user.getFirstName() + " " + user.getLastName();
    }

    public Set<Role> getRoles() {
        return user.getRoles();
    }

}
