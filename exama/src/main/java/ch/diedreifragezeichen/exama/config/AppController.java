package ch.diedreifragezeichen.exama.config;

import ch.diedreifragezeichen.exama.userAdministration.*;

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

    @GetMapping("")
    public String viewHomePage() {
        return "index";
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
    public String processRegistration(User user) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encodedPassword = encoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        user.setEnabled(true);
        user.setLocked(false);
        userRepo.save(user);
        return "/adminTemplates/userSaved";
    }

    // TODO: Edit without unique-Email-Error and without set new password mandatory
    // @GetMapping("/users/edit/{id}")
    // public ModelAndView editUser(@PathVariable(name = "id") Long id) {
    //     User user = userRepo.getUserByID(id);
    //     ModelAndView mav = new ModelAndView("adminTemplates/editUser");
    //     mav.addObject("user", user);
         
    //     List<Role> roles = (List<Role>) roleRepo.findAll();
         
    //     mav.addObject("allRoles", roles);
         
    //     return mav;
    // }
    
    @GetMapping("/users/delete/{id}")
    public String deleteUser(@PathVariable(name = "id") Long id) throws UsernameNotFoundException{
        User user = userRepo.getUserByID(id);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        // TODO: Delete-Part
        return "index";
    }

	@GetMapping("/users/show")
    public String listUsers(Model model) {
        List<User> listUsers = userRepo.findAll();
        model.addAttribute("listUsers", listUsers);

        return "adminTemplates/showUsers";
    }

	@GetMapping("/login")
    public String viewLoginPage() {
        return "login";
    }

    @GetMapping("/error")
    public String handleErrorString() {
        return "403";
    }

}
