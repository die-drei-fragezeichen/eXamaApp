package ch.diedreifragezeichen.exama.userAdministration;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Long> {
    @Query("SELECT u FROM User u WHERE u.email = ?1")
    public User getUserByEmail(String email);

    @Query("SELECT u FROM User u WHERE u.id = ?1")
    public User getUserByID(Long id);

    @Transactional
    @Modifying
    @Query("DELETE User u WHERE u.id = ?1")
    public void deleteUserById(long id);
}