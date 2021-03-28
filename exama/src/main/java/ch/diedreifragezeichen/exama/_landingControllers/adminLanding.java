package ch.diedreifragezeichen.exama._landingControllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class adminLanding {

    @GetMapping("/admin")
    public ModelAndView adminLanding(Model model) {
        ModelAndView mav = new ModelAndView("adminTemplates/index");
        return mav;
    }

    @GetMapping("/systemadmin")
    public ModelAndView systemadminLanding(Model model) {
        // we dont need systemadmin yet -> same than admin
        ModelAndView mav = new ModelAndView("adminTemplates/index");
        return mav;
    }
    
}
