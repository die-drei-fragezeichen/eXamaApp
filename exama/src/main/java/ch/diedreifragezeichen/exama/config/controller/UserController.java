package ch.diedreifragezeichen.exama.config.controller;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import ch.diedreifragezeichen.exama.userAdministration.Role;
import ch.diedreifragezeichen.exama.userAdministration.RoleRepository;
import ch.diedreifragezeichen.exama.userAdministration.User;
import ch.diedreifragezeichen.exama.userAdministration.UserRepository;


@Controller
public class UserController {
    @Autowired
    private UserRepository userRepo;

    @Autowired
    private RoleRepository roleRepo;

    /**
     * User Mappings
     */

    @GetMapping("/users/show")
    public String listUsers(Model model) {
        List<User> listUsers = userRepo.findAll();
        model.addAttribute("listUsers", listUsers);
        return "/adminTemplates/usersShow";
    }

    @GetMapping("/users/create")
    public ModelAndView newUser() {
        User user = new User();
        ModelAndView mav = new ModelAndView("adminTemplates/userCreate");
        mav.addObject("user", user);
        List<Role> roles = (List<Role>) roleRepo.findAll();
        mav.addObject("allRoles", roles);
        return mav;
    }

    @PostMapping("/users/created")
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
    @GetMapping("/users/{email}/edit")
    public ModelAndView editUser(@PathVariable(name = "email") String email) {
        ModelAndView mav = new ModelAndView("adminTemplates/userEdit");
        User user = userRepo.getUserByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        List<Role> roles = (List<Role>) roleRepo.findAll();
        mav.addObject("user", user);
        mav.addObject("allRoles", roles);
        return mav;
    }
    
    @GetMapping("/users/{email}/edited")
    public String updateSubject(@PathVariable(name = "email") String email, @RequestParam(name = "password") String password, @RequestParam(name = "firstName") String firstName,  @RequestParam(name = "lastName") String lastName, @RequestParam(name = "isEnabled", required = false) boolean isEnabled, @RequestParam(name = "isLocked", required = false) boolean isLocked, @RequestParam(name = "roles", required = false) Set<Role> roles) throws UsernameNotFoundException{
        User user = userRepo.getUserByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        if(password.isEmpty()){
            userRepo.editUserByEmail(email, firstName, lastName, isEnabled, isLocked);
        }else{
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            String encodedPassword = encoder.encode(password);
            userRepo.editUserByEmailPW(email, encodedPassword, firstName, lastName, isEnabled, isLocked);
        }
        //TODO: Rollenupdate funktioniert noch nicht
        user.setRoles(roles);
        
        return "redirect:/users/show";
    }

    @GetMapping("/users/{email}/delete")
    public String deleteUser(@PathVariable(name = "email") String email) throws UsernameNotFoundException {
        User user = userRepo.getUserByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        userRepo.deleteUserByEmail(email);
        return "redirect:/users/show";
    }
    
}
