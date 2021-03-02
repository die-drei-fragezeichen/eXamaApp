package ch.diedreifragezeichen.exama.exams;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.persistence.*;
import org.springframework.beans.factory.annotation.Autowired;

import javassist.NotFoundException;

//with the @Service annotation, Spring framework will create an instance of 
//this class as a managed bean in the application context.
//@Service("ExamService")
public class ExamService {

    // With the @Autowired annotation Spring Data JPA will automatically inject a
    // currently "fake" but useable SubjectRepository instance into this
    // ExamService class
    @Autowired
    private ExamRepository examRepo;

    @PersistenceContext
    EntityManager em;

    public Exam loadByID(Long id) throws NotFoundException {
        Exam exam = examRepo.findExamById(id);
        if (exam == null) {
            throw new NotFoundException("Exam not found");
        }
        return exam;
    }

    public List<Exam> loadByDate(Date date) throws NotFoundException {
        List<Exam> dailyExams = examRepo.findAllByExamDate(date);
        if (dailyExams == null) {
            throw new NotFoundException("Exam not found");
        }
        return dailyExams;
    }


    public static Date getFirstDayOfWeek(Date date) {
        Calendar calendar = Calendar.getInstance(Locale.ITALY);
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek());
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return calendar.getTime();
      }


      public static Date getLastDayOfWeek(Date date) {
        Calendar calendar = Calendar.getInstance(Locale.ITALY);
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek() + 6);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        return calendar.getTime();
      }

    public List<Exam> loadExamWeekByDate(Date date) throws NotFoundException {
            
        //DateFormat df = new SimpleDateFormat("EEE dd/MM/yyyy");
        
        
        Date Monday = getFirstDayOfWeek(date);
        Date Sunday = getLastDayOfWeek(date);
        
        List<Exam> allExamsOfWeek = examRepo.findAllByExamDateBetween(Monday, Sunday);
        return allExamsOfWeek;
    }
}

// For further inspiration:
// Find a subject by Id
// Optional<Subject> result = subjectRepo.findById(1L);
// result.ifPresent(subject -> System.out.println(subject.getSubjectName()));

// // Find subject by tag
// Subject wanted = subjectRepo.findSubjectByName("English");
// System.out.println(wanted.getSubjectName());

// // Find subject by tag
// List<Subject> allSubjects = subjectRepo.findSubjectByTag("ENG");
// allSubjects.forEach(subject -> System.out.println(subject.getId()));

// // List all subjects
// Iterable<Subject> iterator = subjectRepo.findAll();
// iterator.forEach(subject -> System.out.println(subject));

// // Count number of subjects
// long countSubjects = subjectRepo.count();
// System.out.println("Number of customers: " + countSubjects);
