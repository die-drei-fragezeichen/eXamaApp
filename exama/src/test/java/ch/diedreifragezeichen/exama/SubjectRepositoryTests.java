/* package ch.diedreifragezeichen.exama;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import ch.diedreifragezeichen.exama.subjects.Subject;
import ch.diedreifragezeichen.exama.subjects.SubjectRepository;


@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class SubjectRepositoryTests {
    @Autowired
    private SubjectRepository subjectRepo;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void testCreateSubject(){
        Subject testSubject = new Subject();
        testSubject.setName("Musik");
        testSubject.setTag("MUS");

        //repo interface provides save method
        Subject savedSubject = subjectRepo.save(testSubject);

        Subject existSubject = entityManager.find(Subject.class, savedSubject.getId());

        assertThat(existSubject.getName().equals(testSubject.getName()));

    }


}
 */