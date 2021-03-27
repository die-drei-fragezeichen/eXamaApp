package ch.diedreifragezeichen.exama._viewControllers;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.time.DayOfWeek;
import java.time.LocalDate;
import ch.diedreifragezeichen.exama.assignments.assignment.*;
import ch.diedreifragezeichen.exama.assignments.exams.Exam;
import ch.diedreifragezeichen.exama.assignments.exams.ExamRepository;
import ch.diedreifragezeichen.exama.assignments.homeworks.Homework;
import ch.diedreifragezeichen.exama.assignments.homeworks.HomeworkRepository;
import ch.diedreifragezeichen.exama.courses.Course;
import ch.diedreifragezeichen.exama.users.User;
import ch.diedreifragezeichen.exama.users.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Controller
public class landingControllerStudent {

    //die benötigten Repositorys laden
    @Autowired
    HomeworkRepository homeworkRepo;

    @Autowired
    ExamRepository examRepo;

    @Autowired
    private UserRepository userRepo;

    @GetMapping("/weekViewStudent/showBene")
    public ModelAndView showWeekStudent(){
        LocalDate today= LocalDate.now();
        String currentUserName = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepo.findUserByEmail(currentUserName);

        ArrayDeque<LocalDate> dates=new ArrayDeque<LocalDate>();
        for(int i=0;i<7;i++){
            LocalDate helpDate=today.with(DayOfWeek.MONDAY).plusDays(i);
            dates.add(helpDate);
        }
        List<Exam> exams=new ArrayList<Exam>();//Liste mit Exams wird für den eingeloggten User erstellt
        for(Course course : user.getCourses()) {
            exams.addAll(examRepo.findAllByCourse(course));
        }
        List<Exam> allExams=exams.stream().filter(x->((x.getDueDate().isAfter(today.with(DayOfWeek.MONDAY))||
        x.getDueDate().isEqual(today.with(DayOfWeek.MONDAY))&&
        (x.getDueDate().isBefore(today.with(DayOfWeek.SUNDAY))||x.getDueDate().isEqual(today.with(DayOfWeek.SUNDAY))))))
        .collect(Collectors.toList());//stream hat alle Exams für die Woche geladen
        allExams=allExams.stream()
        .sorted((e1, e2) -> e1.getDueDate().compareTo(e2.getDueDate()))
        .collect(Collectors.toList());

        List<Homework> homework=new ArrayList<Homework>();//Liste mit Homeowrk wird für den eingeloggten User erstellt
        for(Course course : user.getCourses()) {
            homework.addAll(homeworkRepo.findAllByCourse(course));
        }
        List<Homework> allHomework=homework.stream().filter(x->((x.getDueDate().isAfter(today.with(DayOfWeek.MONDAY))||
        x.getDueDate().isEqual(today.with(DayOfWeek.MONDAY))&&
        (x.getDueDate().isBefore(today.with(DayOfWeek.SUNDAY))||x.getDueDate().isEqual(today.with(DayOfWeek.SUNDAY))))))
        .collect(Collectors.toList());//stream hat alle Homework für die Woche geladen
        // List<Assignment> allAssignment =new ArrayList<Assignment>();
        // allAssignment.addAll(allExams);allAssignment.addAll(allHomework);//jetzt sind alle Hausaufgaben und alle Exams in einer Liste
        //jetzt die Termine noch ordnen:
        allHomework=allHomework.stream()
        .sorted((e1, e2) -> e1.getDueDate().compareTo(e2.getDueDate()))
        .collect(Collectors.toList());
        // allAssignemts der Wochenansicht Student übergeben
        ModelAndView mav=new ModelAndView("studentTemplates/weekViewShowStudent.html");
        mav.addObject("dates", dates);
        mav.addObject("allHomework", allHomework);
        mav.addObject("allExams", allExams);
        //alle Sondertermine anhand von User finden
        return mav;
    }
}
