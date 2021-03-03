package ch.diedreifragezeichen.exama;

// import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.assertj.core.api.Assertions.assertThat;

// import java.io.ByteArrayOutputStream;
// import java.io.PrintStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import ch.diedreifragezeichen.exama.TESTKLASSEN.exams.Exam;
import ch.diedreifragezeichen.exama.TESTKLASSEN.exams.ExamRepository;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class ExamTests {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ExamRepository examRepo;



    /*
     * this test puts an exam into the database this test passes successfully
     */
    @Test
    public void testCreateExam() throws ParseException {
        Exam testExam = new Exam();
        testExam.setName("Shakespeare's fiddle");
        // testExam.setCountingFactor(4.0);
        // testExam.setExamDate(new SimpleDateFormat("yyyy-MM-dd").parse("2021-02-15"));

        Exam savedExam = examRepo.save(testExam);

        Exam existExam = entityManager.find(Exam.class, savedExam.getId());

        assertThat(existExam.getName().equals(testExam.getName()));

    }

    /*
     * this test creates a result list of exams collects all the exams from db
     * beetween given dates this test passes successfully
     */
     @Test
    public void createExamList() throws ParseException {

        Date testMonday = new SimpleDateFormat("yyyy-MM-dd").parse("2021-02-15");
        Date testSunday = new SimpleDateFormat("yyyy-MM-dd").parse("2021-02-21");
        List<Exam> result = examRepo.findAllByExamDateBetween(testMonday, testSunday);

        //result.stream().forEach(s -> System.out.println(s.getName()));

        assertTrue(result.stream().count() == 4);
    }

    /*
     * this test deletes exams by name The test passes successfully
     */
    @Test
    @Transactional
    public void deleteExamByName() {
        long deletedRecords = examRepo.deleteByName("3. Weltkrieg");
        assertThat(deletedRecords).isEqualTo(1);
    }
}