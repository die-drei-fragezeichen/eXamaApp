package ch.diedreifragezeichen.exama.userAdministration;

import org.springframework.security.core.userdetails.UserDetailsService;

import javax.persistence.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class UserDetailsServiceExama implements UserDetailsService {

    @Autowired
    private UserRepository userRepo;

    @PersistenceContext
    EntityManager em;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepo.getUserByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return new UserDetailsExama(user);
    }

    public UserDetails loadUserByID(Long id) throws UsernameNotFoundException {
        User user = userRepo.getUserByID(id);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return new UserDetailsExama(user);
    }

    public void deleteUserByID(Long id) throws UsernameNotFoundException {
        User user = userRepo.getUserByID(id);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        em.createNativeQuery("DELETE FROM users_roles WHERE users_roles.user_id = " + id).executeUpdate();
        em.createNativeQuery("DELETE FROM users WHERE users.id = " + id).executeUpdate();
    }
}