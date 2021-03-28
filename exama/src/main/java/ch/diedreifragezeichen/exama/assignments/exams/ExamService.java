package ch.diedreifragezeichen.exama.assignments.exams;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javassist.NotFoundException;

@Service("ExamService")
public class ExamService {

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
    /*
     * You can use this method if you ever want to find all the exams for a specific
     * date
     */

    // public List<Exam> loadByDate(Date date) throws NotFoundException {
    // List<Exam> dailyExams = examRepo.findAllByExamDate(date);
    // if (dailyExams == null) throw new NotFoundException("Exam not found");
    // return dailyExams;
    // }

    public LocalDate getFirstDayOfWeek(LocalDate date) {
        return date.with(DayOfWeek.MONDAY);
    }

    public LocalDate getLastDayOfWeek(LocalDate date) {
        return date.with(DayOfWeek.SUNDAY);
    }

    public List<Exam> loadExamWeekByDate(LocalDate date) throws NotFoundException {

        LocalDate Monday = getFirstDayOfWeek(date);
        LocalDate Sunday = getLastDayOfWeek(date);

        List<Exam> allExamsOfThisWeek = examRepo.findAllByDueDateBetween(Monday, Sunday);
        return allExamsOfThisWeek;
    }
}