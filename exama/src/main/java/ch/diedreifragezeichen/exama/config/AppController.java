package ch.diedreifragezeichen.exama.config;

import ch.diedreifragezeichen.exama.subject.Subject;
import ch.diedreifragezeichen.exama.subject.SubjectRepository;
import ch.diedreifragezeichen.exama.userAdministration.*;
import javassist.NotFoundException;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AppController {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private RoleRepository roleRepo;

    @Autowired
    private SubjectRepository subjectRepo;

    // //Get the subject by tag. If Spring gets this this mapping, it will carry out
    // the method getSubjectByName from DB
    // @GetMapping("subjects/show")
    // public Subject getSubjectByName(Long id) {
    // return "index";
    // }

    @GetMapping("/users/show")
    public String listUsers(Model model) {
        List<User> listUsers = userRepo.findAll();
        model.addAttribute("listUsers", listUsers);
        return "/adminTemplates/showUsers";
    }

    @GetMapping("/users/edit")
    public ModelAndView newUser() {
        User user = new User();
        ModelAndView mav = new ModelAndView("adminTemplates/editUser");
        mav.addObject("user", user);
        List<Role> roles = (List<Role>) roleRepo.findAll();
        mav.addObject("allRoles", roles);
        return mav;
    }

    @PostMapping("/users/saved")
    public String processSaving(User user) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encodedPassword = encoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        user.setEnabled(true);
        user.setLocked(false);
        userRepo.save(user);
        // returns new mapping command on userSaved.html
        return "redirect:/users/show";
    }

    // TODO: Edit without unique-Email-Error and without set new password mandatory
    @GetMapping("/users/edit/{id}")
    public ModelAndView editUser(@PathVariable(name = "id") Long id) {
        User user = userRepo.getUserByID(id);
        ModelAndView mav = new ModelAndView("adminTemplates/editUser");
        mav.addObject("user", user);
        List<Role> roles = (List<Role>) roleRepo.findAll();
        mav.addObject("allRoles", roles);
        return mav;
    }

    @GetMapping("/users/delete/{id}")
    public String deleteUser(@PathVariable(name = "id") Long id) throws UsernameNotFoundException {
        User user = userRepo.getUserByID(id);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        userRepo.deleteUserById(id);
        return "redirect:/users/show";
    }

    /**
     * Subject Mappings
     */
    @GetMapping("/subjects/show")
    public String listSubjects(Model model) {
        List<Subject> listSubjects = subjectRepo.findAll();
        model.addAttribute("listSubjects", listSubjects);
        return "adminTemplates/showSubjects";
    }

    @GetMapping("/subjects/create")
    public ModelAndView newSubject() {
        Subject subject = new Subject();
        ModelAndView mav = new ModelAndView("adminTemplates/createSubject");
        mav.addObject("subject", subject);
        return mav;
    }
    @PostMapping("/subjects/created")
    public String processSaving(Subject subject) {
        subjectRepo.save(subject);
        return "redirect:/subjects/show";
    }

    @GetMapping("/subjects/edit/{id}")
    public ModelAndView editSubject(@PathVariable(name = "id") Long id) {
        Subject subject = subjectRepo.getSubjectByID(id);
        ModelAndView mav = new ModelAndView("adminTemplates/editSubject");
        mav.addObject("subject", subject);
        return mav;
    }
    @PostMapping("/subjects/edited")
    public String editSubject(long id) {
        subjectRepo.editSubjectById(id);
        return "redirect:/subjects/show";
    }



    @GetMapping("/subjects/delete/{id}")
    public String deleteSubject(@PathVariable(name = "id") Long id) throws NotFoundException {
        Subject subject = subjectRepo.getSubjectByID(id);
        if (subject == null) {
            throw new NotFoundException("Subject not found");
        }
        subjectRepo.deleteSubjectById(id);
        return "redirect:/subjects/show";
    }

    /**
     * Index Mappings
     */
    @GetMapping("")
    public String viewHomePage() {
        return "index";
    }

    /**
     * Login Mappings
     */
    @GetMapping("/login")
    public String viewLoginPage() {
        return "login";
    }

    /**
     * Error Mappings
     */
    @GetMapping("/error")
    public String handleErrorString() {
        return "403";
    }

}
