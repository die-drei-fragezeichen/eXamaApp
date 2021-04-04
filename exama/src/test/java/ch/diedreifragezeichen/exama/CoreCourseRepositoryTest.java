package ch.diedreifragezeichen.exama;

import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

import javax.persistence.EntityManager;

import ch.diedreifragezeichen.exama.courses.CoreCourseRepository;
import ch.diedreifragezeichen.exama.courses.CoreCourse;

@RunWith(SpringRunner.class)
@DataJpaTest
public class CoreCourseRepositoryTest {

    @Autowired
    private CoreCourseRepository testRepo;

    @Autowired
    private EntityManager TestEntityManager;

    @Test
    public void testFindById(){
        //TestCoreCourse wird mit id erzeugt
        CoreCourse testCoreCourse=new CoreCourse();
        long id=(long)1234;
        testCoreCourse.setId(id);
        //TestCoreCourse wird mit mit EntityManager in Datenbank gepackt
        TestEntityManager.persist(testCoreCourse);
        TestEntityManager.flush();

        //Test von Repo
        CoreCourse foundCoreCourse=testRepo.findCoreCourseById(id);
        assertEquals(foundCoreCourse, testCoreCourse);

    }

}
