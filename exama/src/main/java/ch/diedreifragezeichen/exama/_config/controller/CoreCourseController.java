package ch.diedreifragezeichen.exama._config.controller;

import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import javassist.NotFoundException;

import org.hibernate.Session;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ch.diedreifragezeichen.exama.courses.*;
import ch.diedreifragezeichen.exama.userAdministration.RoleRepository;
import ch.diedreifragezeichen.exama.userAdministration.User;
import ch.diedreifragezeichen.exama.userAdministration.UserRepository;

@Controller
public class CoreCourseController {
    @Autowired
    private CoreCourseRepository coreCourseRepo;

    @Autowired
    private CoreCourseService coreCourseService;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private RoleRepository roleRepo;

    private Session session;

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
        List<User> teacherList = userList.stream().filter(c -> c.getRoles().contains(roleRepo.findRoleByName("Teacher"))).collect(Collectors.toList());
        mav.addObject("allTeachers", teacherList);
        return mav;
    }

    @GetMapping("/coreCourses/edit")
    public ModelAndView updateCoreCourse(@RequestParam(name = "id") Long id) {
        ModelAndView mav = new ModelAndView("adminTemplates/coreCourseModify");

        CoreCourse coreCourse = coreCourseRepo.findCoreCourseById(id);
        
        mav.addObject("coreCourse", coreCourse);

        List<User> userList = userRepo.findAll();
        List<User> teacherList = userList;
        mav.addObject("allTeachers", teacherList);

        return mav;
    }

    @PostMapping("/coreCourses/modified")
    @Transactional
    public String modify(CoreCourse coreCourse) {
        //session.saveOrUpdate(coreCourse);
        coreCourseService.saveOrUpdateCoreCourse(coreCourse);
        return "redirect:/coreCourses/show";
    }

    @GetMapping("/coreCourses/delete")
    public String deleteCoreCourse(@RequestParam(name = "id") Long id) {
        coreCourseRepo.deleteById(id);
        return "redirect:/coreCourses/show";
    }
}
