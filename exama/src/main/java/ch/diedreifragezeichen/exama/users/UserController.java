package ch.diedreifragezeichen.exama.users;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.*;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import ch.diedreifragezeichen.exama.courses.*;

@Controller
public class UserController {
    @Autowired
    private UserRepository userRepo;

    @Autowired
    private RoleRepository roleRepo;

    @Autowired
    private CoreCourseRepository coreCourseRepo;

    @Autowired
    private CourseRepository courseRepo;

    @PersistenceContext
    private EntityManager em;

    /**
     * User Mappings
     */

    @GetMapping("/users/show")
    public String show(Model model) {
        List<User> listUsers = userRepo.findAll();
        model.addAttribute("allUsers", listUsers);
        return "adminTemplates/usersShow";
    }

    @GetMapping("/users/create")
    public ModelAndView add() {
        User newUser = new User();
        ModelAndView mav = new ModelAndView("adminTemplates/userCreate");
        mav.addObject("user", newUser);
        List<Role> roleList = roleRepo.findAll();
        mav.addObject("allRoles", roleList);
        List<CoreCourse> coreCourseList = coreCourseRepo.findAll();
        mav.addObject("allCoreCourses", coreCourseList);
        List<Course> courseList = courseRepo.findAll();
        mav.addObject("allCourses", courseList);
        return mav;
    }

    @PostMapping("/users/created")
    @Transactional
    public String create(User user) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encodedPassword = encoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        if(user.getAbbreviation().equals("")){
            user.setAbbreviation(null);
        }
        if(!user.getRoles().contains(roleRepo.findRoleByName("Student"))){
            user.setCoreCourse(null);
        }
        user.setLoggedIn(false);
        user.setCreatedOn(LocalDate.now());
        em.unwrap(org.hibernate.Session.class).saveOrUpdate(user);
        return "redirect:/users/show";
    }

    @GetMapping("/users/edit")
    public ModelAndView edit(@RequestParam(name = "id") Long id) {
        User user = userRepo.findUserById(id);
        ModelAndView mav = new ModelAndView("adminTemplates/userEdit");
        mav.addObject("user", user);
        List<Role> roleList = roleRepo.findAll();
        mav.addObject("allRoles", roleList);
        List<CoreCourse> coreCourseList = coreCourseRepo.findAll();
        mav.addObject("allCoreCourses", coreCourseList);
        List<Course> courseList = courseRepo.findAll();
        mav.addObject("allCourses", courseList);
        return mav;
    }
    
    @PostMapping("/users/edited")
    @Transactional
    public String modify(User user) {
        if(user.getAbbreviation().equals("")){
            user.setAbbreviation(null);
        }
        if(!user.getRoles().contains(roleRepo.findRoleByName("Student"))){
            user.setCoreCourse(null);
        }
        em.unwrap(org.hibernate.Session.class).saveOrUpdate(user);
        return "redirect:/users/show";
    }

    @GetMapping("/users/resetPassword")
    public ModelAndView resetPassword(@RequestParam(name = "id") Long id) {
        User user = userRepo.findUserById(id);
        ModelAndView mav = new ModelAndView("adminTemplates/userResetPassword");
        mav.addObject("user", user);
        return mav;
    }

    @PostMapping("/users/passwordReseted")
    @Transactional
    public String passwordReseted(User user) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encodedPassword = encoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        em.unwrap(org.hibernate.Session.class).saveOrUpdate(user);
        return "redirect:/users/show";
    }

    @GetMapping("/users/delete")
    public String delete(@RequestParam(name = "id") Long id) {
        userRepo.deleteById(id);
        return "redirect:/users/show";
    }

    @GetMapping("/users/changePassword")
    public ModelAndView changePassword() {
        ModelAndView mav = new ModelAndView("generalTemplates/changePassword");
        Authentication authLoggedInUser = SecurityContextHolder.getContext().getAuthentication();
        String currentUserName = authLoggedInUser.getName();
        User user = userRepo.findUserByEmail(currentUserName);
        mav.addObject("user", user);
        return mav;
    }

    @PostMapping("/users/passwordChanged")
    @Transactional
    public String passwordChanged(User user) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encodedPassword = encoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        em.unwrap(org.hibernate.Session.class).saveOrUpdate(user);
        return "generalTemplates/passwordChanged";
    }
}