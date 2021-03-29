package ch.diedreifragezeichen.exama._viewControllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import java.time.*;
import java.util.*;

import javax.persistence.*;

import org.springframework.beans.factory.annotation.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;


import ch.diedreifragezeichen.exama.assignments.exams.Exam;
import ch.diedreifragezeichen.exama.assignments.exams.ExamRepository;
import ch.diedreifragezeichen.exama.assignments.exams.ExamService;

import ch.diedreifragezeichen.exama.operator.Operator;
import ch.diedreifragezeichen.exama.operator.OperatorRepository;
import ch.diedreifragezeichen.exama.subjects.Subject;


@Controller
public class examBarController {

    @Autowired
    private ExamRepository examRepo;

    @Autowired
    private OperatorRepository operatorRepo;


    @PersistenceContext
    private EntityManager em;

    /*
     * These parts handle the examBar /*
     * 
     * Arriving at this path creates a mav bean that is filled with a link and a
     * datum object that can now be filled on HTML by user
     */
    @GetMapping("/examBar/selectDate")
    public ModelAndView selectExamBarDate() {

        Operator datum = new Operator();
        ModelAndView mav = new ModelAndView("studentTemplates/examBarSelectDate");
        mav.addObject("datum", datum);
        return mav;
    }

    /**
     * When User click submit button, this method is execuded. Te datum object is
     * saved into db by operatorRepo o note that this link is different from above,
     * but it is never really shown, user is directly redirected.
     */
    @PostMapping("/examBar/DateSelected")
    public String processSelectedDate(Operator datum) {
        operatorRepo.save(datum);
        return "redirect:/examBar/show";
    }

    /**
     * Fetches All exams based on the selected Datum object
     */
    @RequestMapping("/examBar/show")
    public String showExamBarForSavedDate(Model model) {
        Operator selectedDatum = operatorRepo.findAll().get(0);
        operatorRepo.deleteAll();
        LocalDate selectedDate = selectedDatum.getSelectedDate();
        model.addAttribute("Datum", selectedDate);

        ExamService helper = new ExamService();
        LocalDate Monday = helper.getFirstDayOfWeek(selectedDate);
        model.addAttribute("Montag", Monday);

        LocalDate Sunday = helper.getLastDayOfWeek(selectedDate);
        model.addAttribute("Sonntag", Sunday);

        List<Exam> listExams = examRepo.findAllByDueDateBetween(Monday, Sunday);
        model.addAttribute("liste", listExams);

        calculateNumberOfExams(model, listExams);
        calculateExamFactor(model, listExams);

        return "studentTemplates/examBarShow";

    }

    /**
     * Hilfsmethode 1
     */

    public Model calculateNumberOfExams(Model model, List<Exam> listExams) {
        // NOTE: Thymeleaf isn't Velocity or Freemarker and doesn't replace expressions
        // blindly. You need the expression in an appropriate tag attribute, such as
        // <h1 data-th-text="${data}" />

        model.addAttribute("msg", "Anzahl Leistungsmessungen diese Woche: ");
        long anzahlExamen = listExams.stream().count();
        model.addAttribute("anzpr", anzahlExamen);
        // Schreibe im html: <h1 data-th-text="${anzpr}" />
        return model;
    }

    /**
     * Hilfsmethode 2
     */

    public Model calculateExamFactor(Model model, List<Exam> listExams) {

        model.addAttribute("msg2", "Insgesamt zählt das mit einem Belastungsfaktor von: ");
        double ExamFactor = listExams.stream().mapToDouble(exam -> exam.getCountingFactor()).sum();
        model.addAttribute("xFactor", ExamFactor);
        // Schreibe im html: <h1 data-th-text="${anzpr}" />
        return model;
    }

    /**
     * Hilfsmethode 3
     */

    public ModelAndView calculateNumberOfSubjects(ModelAndView mav, List<Subject> allSubjects) {
        long numberOfSubjects = allSubjects.stream().count();
        mav.addObject("numberOfSubjects", numberOfSubjects);
        return mav;
    }

}
