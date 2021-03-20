package ch.diedreifragezeichen.exama.courses;

import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ch.diedreifragezeichen.exama.users.*;

@Controller
public class CoreCourseController {
    @Autowired
    private CoreCourseRepository coreCourseRepo;

    @Autowired
    private UserRepository userRepo;
    
    @Autowired
    private RoleRepository roleRepo;

    @PersistenceContext
    private EntityManager em;

    /**
     * CoreCourse Mappings
     */

    @GetMapping("/coreCourses/show")
    public String showCoreCourses(Model model) {
        List<CoreCourse> listCoreCourses = coreCourseRepo.findAll();
        model.addAttribute("allCoreCourses", listCoreCourses);
        return "adminTemplates/coreCoursesShow";
    }

    @GetMapping("/coreCourses/create")
    public ModelAndView newCoreCourse() {
        ModelAndView mav = new ModelAndView("adminTemplates/coreCourseModify");
        CoreCourse newCoreCourse = new CoreCourse();
        mav.addObject("coreCourse", newCoreCourse);
        List<User> userList = userRepo.findAll();
        List<User> teacherList = userList.stream()
                .filter(c -> c.getRoles().contains(roleRepo.findRoleByName("Teacher"))).collect(Collectors.toList());
        mav.addObject("allTeachers", teacherList);
        return mav;
    }

    @GetMapping("/coreCourses/edit")
    public ModelAndView updateCoreCourse(@RequestParam(name = "id") Long id) {
        ModelAndView mav = new ModelAndView("adminTemplates/coreCourseModify");
        CoreCourse coreCourse = coreCourseRepo.findCoreCourseById(id);
        mav.addObject("coreCourse", coreCourse);
        List<User> userList = userRepo.findAll();
        List<User> teacherList = userList.stream()
        .filter(c -> c.getRoles().contains(roleRepo.findRoleByName("Teacher"))).collect(Collectors.toList());
        mav.addObject("allTeachers", teacherList);
        return mav;
    }

    @PostMapping("/coreCourses/modified")
    @Transactional
    public String modify(CoreCourse coreCourse) {
        em.unwrap(org.hibernate.Session.class).saveOrUpdate(coreCourse);
        return "redirect:/coreCourses/show";
    }

    @GetMapping("/coreCourses/delete")
    public String deleteCoreCourse(@RequestParam(name = "id") Long id) {
        coreCourseRepo.deleteById(id);
        return "redirect:/coreCourses/show";
    }
}