package ch.diedreifragezeichen.exama._viewControllers;

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

    @GetMapping("/weekViewStudent/show")
    //@GetMapping("/weekViewStudent/nextWeekStudent/weekViewStudent/show")
    public ModelAndView showStudentThisWeek(){
        LocalDate today= LocalDate.now();
        return showWeekStudent(today, "studentTemplates/weekViewShowStudent.html");
    }
    @GetMapping("nextWeekStudent/show")
    public ModelAndView showStudentNextWeek(){
        LocalDate today= LocalDate.now().plusDays(7);
        return showWeekStudent(today, "studentTemplates/weekViewShowStudentNext.html");
    }


    public ModelAndView showWeekStudent(LocalDate date, String URL){
        LocalDate today= date;
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
        for(int i=0;i<7;i++){
            LocalDate weekday =today.with(DayOfWeek.MONDAY).plusDays(i);
            List<Exam> exam=exams.stream().filter(x->x.getDueDate().equals(weekday)).collect(Collectors.toList());
            //stream hat alle Exams für den i-ten Wochentag in eine Liste geladen geladen
            List<Homework> homework=homeworks.stream().filter(x->x.getDueDate().equals(weekday)).collect(Collectors.toList());
            //stream hat alle homeworks für den i-ten Wochentag in eine Liste geladen geladen
            dates.add(weekday); allExamsByWeekday.add(exam);allHomeworkByWeekday.add(homework);
        }
        ModelAndView mav=new ModelAndView(URL);
        mav.addObject("dates", dates);
        mav.addObject("allHomework", allHomeworkByWeekday);
        mav.addObject("allExams", allExamsByWeekday);
        mav.addObject("name", user);
        return mav;
    }

}
