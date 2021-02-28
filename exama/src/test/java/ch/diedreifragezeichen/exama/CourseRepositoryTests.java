/* package ch.diedreifragezeichen.exama;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import ch.diedreifragezeichen.exama.courses.Course;
import ch.diedreifragezeichen.exama.courses.CourseRepository;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class CourseRepositoryTests {
    @Autowired
    private CourseRepository courseRepo;

    @Autowired
    private TestEntityManager entityManager;

    //successful test
    @Test
    public void testCreateCourse(){
        Course testCourse = new Course();
        testCourse.setName("1998f");
        testCourse.setEnabled(true);

        //repo interface provides save method
        Course savedCourse = courseRepo.save(testCourse);

        Course existCourse = entityManager.find(Course.class, savedCourse.getId());

        assertThat(existCourse.getName().equals(testCourse.getName()));

    }


}
 */