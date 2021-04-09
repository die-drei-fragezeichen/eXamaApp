package ch.diedreifragezeichen.exama._landingControllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import ch.diedreifragezeichen.exama._services.AppService;
import ch.diedreifragezeichen.exama.users.User;

@Controller
public class studentLanding {
    @Autowired
    AppService helper;
    
    // @GetMapping("/student")
    // public ModelAndView studentLandingPage(Model model) {
    //     ModelAndView mav = new ModelAndView("studentTemplates/index");
    //     return mav;
    // }

    // @GetMapping("/rstudent")
    // public ModelAndView rstudentLandingPage(Model model) {
    //     // we dont need referencestudents yet -> same than student
    //     ModelAndView mav = new ModelAndView("studentTemplates/index");
    //     return mav;
    // }
    
    @GetMapping("/student")
    public String studentLandingPage() {
        if(helper.getCurrentUser() != null){
            User thisUser = helper.getCurrentUser();
            return "redirect:/calendar?view=1&coreCourse="+thisUser.getCoreCourse().getId().toString();
        }
        return "redirect:/";
    }

    @GetMapping("/rstudent")
    public String rstudentLandingPage() {
        if(helper.getCurrentUser() != null){
            User thisUser = helper.getCurrentUser();
            return "redirect:/calendar?view=1&coreCourse="+thisUser.getCoreCourse().getId().toString();
        }
        return "redirect:/";
    }
}
