package ch.diedreifragezeichen.exama.users;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, Long> {
    @Query("SELECT u FROM User u WHERE u.email = ?1")
    public User getUserByEmail(String email);

    @Query("SELECT u FROM User u WHERE u.id = ?1")
    public User getUserByID(Long id);

    @Transactional
    @Modifying
    @Query("DELETE User u WHERE u.id = ?1")
    public void deleteUserById(long id);

    @Transactional
    @Modifying
    @Query("DELETE User u WHERE u.email = ?1")
    public void deleteUserByEmail(String email);

    // Update User including password (email cannot be changed, because it's the
    // unique identifier)
    @Transactional
    @Modifying
    @Query("UPDATE User u SET u.password=?2, u.firstName=?3, u.lastName=?4, u.enabled=?5 WHERE u.email=?1")
    public void editUserByEmailPW(String email, String password, String firstName, String lastName, boolean isEnabled);

    // Update User including password (email cannot be changed, because it's the
    // unique identifier)
    @Transactional
    @Modifying
    @Query("UPDATE User u SET u.firstName=?2, u.lastName=?3, u.enabled=?4 WHERE u.email=?1")
    public void editUserByEmail(String email, String firstName, String lastName, boolean isEnabled);
    
    @Transactional
    @Modifying
    @Query("UPDATE User u SET u.password=?2 WHERE u.email=?1")
    public void changeUserPassword(String email, String password);
}