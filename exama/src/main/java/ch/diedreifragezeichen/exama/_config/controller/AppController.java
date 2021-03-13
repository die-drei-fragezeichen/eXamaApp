package ch.diedreifragezeichen.exama._config.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import ch.diedreifragezeichen.exama.userAdministration.User;
import ch.diedreifragezeichen.exama.userAdministration.UserRepository;

@Controller
public class AppController {

    // //Get the subject by tag. If Spring gets this this mapping, it will carry out
    // the method getSubjectByName from DB
    // @GetMapping("subjects/show")
    // public Subject getSubjectByName(Long id) {
    // return "index";
    // }
    @Autowired
    UserRepository userRepo;

    /**
     * Index Mappings
     */
    @SuppressWarnings("unchecked")
    @GetMapping("/")
    public String viewRolespecificLanding() {
        Authentication authLoggedInUser = SecurityContextHolder.getContext().getAuthentication();
        if (!(authLoggedInUser instanceof AnonymousAuthenticationToken)) {
            String currentUserName = authLoggedInUser.getName();
            User user = userRepo.getUserByEmail(currentUserName);
            user.setLoggedIn(true);
            user.setLastLogin(LocalDate.now());
            userRepo.save(user);
        }

        Collection<SimpleGrantedAuthority> roleList = (Collection<SimpleGrantedAuthority>) authLoggedInUser
                .getAuthorities();
        List<String> roles = new ArrayList<String>();
        for (SimpleGrantedAuthority role : roleList) {
            roles.add(role.getAuthority());
        }
        if (roles.contains("SYSTEMADMIN")) {
            return "systemadminTemplates/index";
        } else if (roles.contains("ADMIN")) {
            return "adminTemplates/index";
        } else if (roles.contains("TEACHER")) {
            return "teacherTemplates/index";
        } else if (roles.contains("REFERENCESTUDENT")) {
            return "rstudentTemplates/index";
        } else if (roles.contains("STUDENT")) {
            return "studentTemplates/index";
        } else {
            return "index";
        }
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

    /**
     * Installation commands
     */
    @GetMapping("/install")
    public String install() {
        return "redirect:/login";
    }

    /**
     * Settings commands
     */
    @GetMapping("/settings")
    public String settings() {
        return "generalTemplates/settings";
    }

}
