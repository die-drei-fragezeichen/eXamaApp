package ch.diedreifragezeichen.exama.assignments.mime;

//import java.text.DateFormat;
//import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
//import java.util.Calendar;
//import java.util.Date;
import java.util.List;
//import java.util.Locale;

import javax.persistence.*;
import org.springframework.beans.factory.annotation.Autowired;

import javassist.NotFoundException;

//with the @Service annotation, Spring framework will create an instance of 
//this class as a managed bean in the application context.
//@Service("MimeService")
public class MimeService {

    // With the @Autowired annotation Spring Data JPA will automatically inject a
    // currently "fake" but useable SubjectRepository instance into this
    // MimeService class
    @Autowired
    private MimeRepository mimeRepo;

    @PersistenceContext
    EntityManager em;

    public Mime loadByID(Long id) throws NotFoundException {
        Mime mime = mimeRepo.findMimeById(id);
        if (mime == null) {
            throw new NotFoundException("Mime not found");
        }
        return mime;
    }

    // public List<Mime> loadByDate(Date date) throws NotFoundException {
    // List<Mime> dailyMimes = mimeRepo.findAllByMimeDate(date);
    // if (dailyMimes == null) {
    // throw new NotFoundException("Mime not found");
    // }
    // return dailyMimes;
    // }

    public LocalDate getFirstDayOfWeek(LocalDate date) {
        return date.with(DayOfWeek.MONDAY);
    }

    public LocalDate getLastDayOfWeek(LocalDate date) {
        return date.with(DayOfWeek.SUNDAY);

        // comment which I don't understand yet:
        // Now let's again set the date to Sunday, but this time in a localized way...
        // the method dayOfWeek() uses localized numbering (Sunday = 1 in US and = 7 in
        // France)
        // System.out.println(ld.with(WeekFields.of(Locale.US).dayOfWeek(), 1L)); //
        // 2017-08-13
        // System.out.println(ld.with(WeekFields.of(Locale.FRANCE).dayOfWeek(), 7L)); //
        // 2017-08-20
    }


    public List<Mime> loadMimeWeekByDate(LocalDate date) throws NotFoundException {

        // //DateFormat df = new SimpleDateFormat("EEE dd/MM/yyyy");

        LocalDate Monday = getFirstDayOfWeek(date);
        LocalDate Sunday = getLastDayOfWeek(date);

        List<Mime> allMimesOfThisWeek = mimeRepo.findAllByEditDateBetween(Monday, Sunday);
        return allMimesOfThisWeek;
    }
}

// For further inspiration:

/*
 * // // Count number of subjects
 */

// long countSubjects = subjectRepo.count();
// System.out.println("Number of customers: " + countSubjects);

// For further inspiration:
// Find a subject by Id
// Optional<Subject> result = subjectRepo.findById(1L);
// result.ifPresent(subject -> System.out.println(subject.getSubjectName()));

// // Find subject by tag
// Subject wanted = subjectRepo.findSubjectByName("English");
// System.out.println(wanted.getSubjectName());
/*
 * // Find subject by tag
 */
// List<Subject> allSubjects = subjectRepo.findSubjectByTag("ENG");
// allSubjects.forEach(subject -> System.out.println(subject.getId()));