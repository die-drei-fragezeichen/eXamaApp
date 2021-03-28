package ch.diedreifragezeichen.exama._landingControllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class studentLanding {

    @GetMapping("/student")
    public ModelAndView studentLanding(Model model) {
        ModelAndView mav = new ModelAndView("studentTemplates/index");
        return mav;
    }

    @GetMapping("/rstudent")
    public ModelAndView rstudentLanding(Model model) {
        // we dont need referencestudents yet -> same than student
        ModelAndView mav = new ModelAndView("studentTemplates/index");
        return mav;
    }
    
}
