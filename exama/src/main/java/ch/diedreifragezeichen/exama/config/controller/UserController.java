package ch.diedreifragezeichen.exama.config.controller;

import java.util.List;

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
    @GetMapping("/users/{id}/edit")
    public ModelAndView editUser(@PathVariable(name = "id") Long id) {
        User user = userRepo.getUserByID(id);
        ModelAndView mav = new ModelAndView("adminTemplates/userEdit");
        mav.addObject("user", user);
        List<Role> roles = (List<Role>) roleRepo.findAll();
        mav.addObject("allRoles", roles);
        return mav;
    }

    @GetMapping("/users/{id}/delete")
    public String deleteUser(@PathVariable(name = "id") Long id) throws UsernameNotFoundException {
        User user = userRepo.getUserByID(id);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        userRepo.deleteUserById(id);
        return "redirect:/users/show";
    }
    
}
