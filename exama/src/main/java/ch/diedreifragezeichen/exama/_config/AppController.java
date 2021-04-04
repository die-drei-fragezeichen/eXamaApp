package ch.diedreifragezeichen.exama._config;

import static java.time.DayOfWeek.FRIDAY;
import static java.time.DayOfWeek.MONDAY;
import static java.time.DayOfWeek.THURSDAY;
import static java.time.DayOfWeek.TUESDAY;
import static java.time.DayOfWeek.WEDNESDAY;
import static java.time.temporal.TemporalAdjusters.nextOrSame;
import static java.time.temporal.TemporalAdjusters.previousOrSame;

import java.time.DayOfWeek;
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
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import ch.diedreifragezeichen.exama.users.User;
import ch.diedreifragezeichen.exama.users.UserRepository;

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
    public String viewRolespecificLanding(Model model) {
        Authentication authLoggedInUser = SecurityContextHolder.getContext().getAuthentication();
        if (!(authLoggedInUser instanceof AnonymousAuthenticationToken)) {
            String currentUserName = authLoggedInUser.getName();
            User user = userRepo.findUserByEmail(currentUserName);
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

        // Systemadmins or admins, that are also teachers will choose there Landing
        // (teacher or admin)
        if ((roles.contains("SYSTEMADMIN") || roles.contains("ADMIN")) && roles.contains("TEACHER")) {
            return "adminTemplates/chooseLanding";

            // only Admins will land on adminLanding
        } else if (roles.contains("ADMIN")) {
            return "redirect:admin";

            // only Teachers will land on teacherLanding
        } else if (roles.contains("TEACHER")) {
            return "redirect:teacher?day=" + LocalDate.now().with(DayOfWeek.MONDAY);

            // students or ReferenceStudents will land on studentLanding
        } else if (roles.contains("REFERENCESTUDENT") || roles.contains("STUDENT")) {
            return "redirect:student?view=1&day=" + LocalDate.now().with(DayOfWeek.MONDAY);

            // everyone without any of the defined autorities
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
    @GetMapping("errorTempates/error")
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

    /**
     * Go to own profile page
     */
    @GetMapping("/profile")
    public String settings2(Model model) {
        Authentication authLoggedInUser = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepo.findUserByEmail(authLoggedInUser.getName());
        model.addAttribute("user", user);
        return "generalTemplates/profile2";
    }

}
