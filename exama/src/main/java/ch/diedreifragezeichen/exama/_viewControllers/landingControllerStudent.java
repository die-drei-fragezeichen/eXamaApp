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
import ch.diedreifragezeichen.exama.assignments.exams.Exam;
import ch.diedreifragezeichen.exama.assignments.exams.ExamRepository;
import ch.diedreifragezeichen.exama.assignments.homeworks.Homework;
import ch.diedreifragezeichen.exama.assignments.homeworks.HomeworkRepository;
import ch.diedreifragezeichen.exama.courses.Course;
import ch.diedreifragezeichen.exama.users.User;
import ch.diedreifragezeichen.exama.users.UserRepository;
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
        
        List<Exam> exams=new ArrayList<Exam>();//Liste mit Exams wird für den eingeloggten User erstellt
        List<Homework> homeworks=new ArrayList<Homework>();//Liste mit Homeowrk wird für den eingeloggten User erstellt
        for(Course course : user.getCourses()) {
            exams.addAll(examRepo.findAllByCourse(course));
            homeworks.addAll(homeworkRepo.findAllByCourse(course));
        }
        
        List<List> allExamsByWeekday=new ArrayList<List>();//Liste für die Listen der Exams
        List<List> allHomeworkByWeekday=new ArrayList<List>();//Liste für die Listen der Homework
        List<LocalDate> dates=new ArrayList<LocalDate>();
        
        List<String> examString=new ArrayList<String>();//nur zum Test: soll alle Namen der Examen als String speichern
        for(int i=0;i<7;i++){
            LocalDate weekday =today.with(DayOfWeek.MONDAY).minusDays(i);//muss daraus nachher plus machen
            List<Exam> exam=exams.stream().filter(x->x.getDueDate().equals(weekday)).collect(Collectors.toList());
            //stream hat alle Exams für den i-ten Wochentag in eine Liste geladen geladen
            List<Homework> homework=homeworks.stream().filter(x->x.getDueDate().equals(weekday)).collect(Collectors.toList());
            //stream hat alle homeworks für den i-ten Wochentag in eine Liste geladen geladen
            dates.add(weekday);
            allExamsByWeekday.add(exam);
            allHomeworkByWeekday.add(homework);
            
            for (Exam e : exam) {//kann weg, wenn es läuft
                examString.add(e.getName());  
            }
        }
        ModelAndView mav=new ModelAndView("studentTemplates/weekViewShowStudent.html");
        mav.addObject("dates", dates);
        mav.addObject("allHomework", allHomeworkByWeekday);
        mav.addObject("allExams", allExamsByWeekday);
        mav.addObject("examString", examString);
        //alle Sondertermine anhand von User finden
        return mav;
    }
}
