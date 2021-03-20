package ch.diedreifragezeichen.exama.assignments.homeworks;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import javax.persistence.*;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import ch.diedreifragezeichen.exama.assignments.availablePrepTimes.AvailablePrepTimeRepository;
import ch.diedreifragezeichen.exama.assignments.workload.WorkloadDistributionRepository;
import ch.diedreifragezeichen.exama.assignments.workload.WorkloadDistribution;
import ch.diedreifragezeichen.exama.courses.Course;
import ch.diedreifragezeichen.exama.courses.CourseRepository;
import ch.diedreifragezeichen.exama.users.User;
import ch.diedreifragezeichen.exama.users.UserRepository;

@Controller
public class HomeworkController {
    @Autowired
    private HomeworkRepository homeworkRepo;

    @Autowired
    private CourseRepository courseRepo;
    
    @Autowired
    private UserRepository userRepo;

    @Autowired
    private AvailablePrepTimeRepository preptimeRepo;

    @Autowired
    private WorkloadDistributionRepository distributionRepo;
    
    @PersistenceContext
    private EntityManager em;

    /**
     * Homework Mappings
     */

    @GetMapping("/homeworks/show")
    public String show(Model model) {
        List<Homework> listHomeworks = homeworkRepo.findAll();
        model.addAttribute("allHomeworks", listHomeworks);
        return "teacherTemplates/homeworksShow";
    }
    
    @GetMapping("/homeworks/create")
    public ModelAndView add(){
        Authentication authLoggedInUser = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepo.findUserByEmail(authLoggedInUser.getName());
        ModelAndView mav = new ModelAndView("teacherTemplates/homeworkModify");
        Homework newHomework = new Homework();
        mav.addObject("homework", newHomework);
        List<Course> listCourses = courseRepo.findAll();
        List<Course> usersCourses = listCourses.stream().filter(c -> c.getUsers().contains(user)).collect(Collectors.toList());
        mav.addObject("allCourses", usersCourses);
        List<WorkloadDistribution> listDist = distributionRepo.findAll();
        mav.addObject("allWorkloadDistributions", listDist);
        return mav;
    }

    @GetMapping("/homeworks/edit")
    public ModelAndView edit(@RequestParam(name = "id") Long id){
        Authentication authLoggedInUser = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepo.findUserByEmail(authLoggedInUser.getName());
        ModelAndView mav = new ModelAndView("teacherTemplates/homeworkModify");
        Homework homework = homeworkRepo.findHomeworkById(id);
        mav.addObject("homework", homework);
        List<Course> listCourses = courseRepo.findAll();
        List<Course> usersCourses = listCourses.stream().filter(c -> c.getUsers().contains(user)).collect(Collectors.toList());
        mav.addObject("allCourses", usersCourses);
        List<WorkloadDistribution> listDist = distributionRepo.findAll();
        mav.addObject("allWorkloadDistributions", listDist);
        return mav;
    }

    @PostMapping("homeworks/modified")
    @Transactional
    public String modify(Homework homework){
        Authentication authLoggedInUser = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepo.findUserByEmail(authLoggedInUser.getName());
        homework.setCreator(user);
        homework.setEditDate(LocalDate.now());
        homework.setStartDate(LocalDate.now());
        homework.setAvailablePrepTime(preptimeRepo.findAvailablePrepTimeById(1l));
        em.unwrap(org.hibernate.Session.class).saveOrUpdate(homework);
        return "redirect:/homeworks/show";
    }

    @GetMapping("/homeworks/delete")
    public String delete(@RequestParam(name = "id") Long id) {
        homeworkRepo.deleteById(id);
        return "redirect:/homeworks/show";
    }
}
