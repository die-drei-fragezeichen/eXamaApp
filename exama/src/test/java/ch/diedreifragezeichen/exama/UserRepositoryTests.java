package ch.diedreifragezeichen.exama;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import ch.diedreifragezeichen.exama.subjects.Subject;
import ch.diedreifragezeichen.exama.subjects.SubjectRepository;
import ch.diedreifragezeichen.exama.userAdministration.Role;
import ch.diedreifragezeichen.exama.userAdministration.RoleRepository;
import ch.diedreifragezeichen.exama.userAdministration.User;
import ch.diedreifragezeichen.exama.userAdministration.UserRepository;


@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class UserRepositoryTests {
    @Autowired
    private UserRepository userRepo;

    @Autowired 
    private RoleRepository roleRepo;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void testCreateUser(){
        List<Role> testRoleList = roleRepo.findAll();
        Set<Role> testRoleSet = new HashSet<Role>();
        for (Role x : testRoleList) 
            testRoleSet.add(x);

        User user = new User();
        user.setEmail("lazyboy@gmail.com");
        user.setPassword("lazy2020");
        user.setFirstName("lazy");
        user.setLastName("boy");
        user.setEnabled(true);
        user.setRoles(testRoleSet);


        //repo interface provides save method
        User savedUser = userRepo.save(user);

        User existUser = entityManager.find(User.class, savedUser.getId());

        assertThat(existUser.getEmail().equals(user.getEmail()));

    }

    // @Test
    // public void testFindUserByEmail(){
    //     String email ="hammer@gmail.com";

    //     User user = userRepo.findByEmail(email);
    //     //makes sure that the email has been found
    //     assertThat(user).isNotNull();
    // }


    // @Test
    // public void testFindUserByRole(){
    //     String email ="brim@gmail.com";

    //     User user = repo.findByRole(email);
    //     //makes sure that the role has been found
    //     assertThat(user.getRole().equals("boss"));
    // }


}
