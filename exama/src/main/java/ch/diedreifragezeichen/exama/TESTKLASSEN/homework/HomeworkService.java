package ch.diedreifragezeichen.exama.TESTKLASSEN.homework;

//import java.text.DateFormat;
//import java.text.SimpleDateFormat;
//import java.time.DayOfWeek;
//import java.time.LocalDate;
//import java.util.Calendar;
//import java.util.Date;
//import java.util.List;
//import java.util.Locale;

import javax.persistence.*;
import org.springframework.beans.factory.annotation.Autowired;

import javassist.NotFoundException;

//with the @Service annotation, Spring framework will create an instance of 
//this class as a managed bean in the application context.
//@Service("HomeworkService")
public class HomeworkService {
    
    // With the @Autowired annotation Spring Data JPA will automatically inject a
    // currently "fake" but useable SubjectRepository instance into this
    // HomeworkService class
    @Autowired
    private HomeworkRepository homeworkRepo;

    @PersistenceContext
    EntityManager em;

    public Homework loadByID(Long id) throws NotFoundException {
        Homework homework = homeworkRepo.findHomeworkById(id);
        if (homework == null) {
            throw new NotFoundException("Homework not found");
        }
        return homework;
    }
}
