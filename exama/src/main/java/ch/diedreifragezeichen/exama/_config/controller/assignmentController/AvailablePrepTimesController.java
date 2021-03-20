package ch.diedreifragezeichen.exama._config.controller.assignmentController;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import ch.diedreifragezeichen.exama.assignments.availablePrepTimes.AvailablePrepTime;
import ch.diedreifragezeichen.exama.assignments.availablePrepTimes.AvailablePrepTimeRepository;
import javassist.NotFoundException;

@Controller
public class AvailablePrepTimesController {
    @Autowired
    private AvailablePrepTimeRepository prepTimeRepo;

    @PersistenceContext
    private EntityManager em;

    /**
     * ExamType Mappings
     */

    @GetMapping("/preptimes/show")
    public String listTypes(Model model) {
        List<AvailablePrepTime> listPrepTimes = prepTimeRepo.findAll();
        model.addAttribute("listPrepTimes", listPrepTimes);
        return "adminTemplates/preptimesShow";
    }

    @GetMapping("/preptimes/create")
    public ModelAndView newPrepTime() {
        AvailablePrepTime prepTime = new AvailablePrepTime();
        ModelAndView mav = new ModelAndView("adminTemplates/preptimeModify");
        mav.addObject("prepTime", prepTime);
        return mav;
    }

    @GetMapping("/preptimes/edit")
    public ModelAndView updateAvailablePreptime(@RequestParam(name = "id") Long id) {
        ModelAndView mav = new ModelAndView("adminTemplates/preptimeModify");
        AvailablePrepTime prepTime = prepTimeRepo.findAvailablePrepTimeById(id);
        mav.addObject("prepTime", prepTime);
        return mav;
    }

    @PostMapping("/preptimes/modified")
    @Transactional
    public String modify(AvailablePrepTime prepTime) {
        em.unwrap(org.hibernate.Session.class).saveOrUpdate(prepTime);
        return "redirect:/preptimes/show";
    }

    @GetMapping("preptimes/{id}/delete")
    public String deletePrepTime(@PathVariable(name = "id") Long id) throws NotFoundException {
        prepTimeRepo.deleteById(id);
        return "redirect:/preptimes/show";
    }
}