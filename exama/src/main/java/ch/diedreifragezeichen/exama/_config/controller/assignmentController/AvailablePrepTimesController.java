package ch.diedreifragezeichen.exama._config.controller.assignmentController;

import java.util.List;

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
        ModelAndView mav = new ModelAndView("adminTemplates/preptimeCreate");
        mav.addObject("prepTime", prepTime);
        return mav;
    }

    @PostMapping("/preptimes/created")
    public String processSaving(AvailablePrepTime prepTime) {
        prepTimeRepo.save(prepTime);
        return "redirect:/preptimes/show";
    }

    @GetMapping("/preptimes/{id}/edit")
    public ModelAndView editPrepTime(@PathVariable(name = "id") Long id) throws NotFoundException {
        AvailablePrepTime prepTime = prepTimeRepo.getPrepTimeByID(id);
        if (prepTime == null) {
            throw new NotFoundException("PrepTime not found");
        }
        ModelAndView mav = new ModelAndView("adminTemplates/preptimeEdit");
        mav.addObject("prepTime", prepTime);
        return mav;
    }

    @GetMapping("preptimes/{id}/edited")
    public String updatePrepTime(@PathVariable(name = "id") Long id, @RequestParam(name = "name") String name,
            @RequestParam(name = "days") int days) throws NotFoundException {
        AvailablePrepTime prepTime = prepTimeRepo.getPrepTimeByID(id);
        if (prepTime == null) {
            throw new NotFoundException("PrepTime not found");
        }
        if (id == 1) {
            return "redirect:/preptimes/show";
        }
        prepTimeRepo.editPrepTimeById(id, name, (int) days);
        return "redirect:/preptimes/show";
    }

    @GetMapping("preptimes/{id}/delete")
    public String deletePrepTime(@PathVariable(name = "id") Long id) throws NotFoundException {
        AvailablePrepTime prepTime = prepTimeRepo.getPrepTimeByID(id);
        if (prepTime == null) {
            throw new NotFoundException("PrepTime not found");
        }
        prepTimeRepo.deletePrepTimeById(id);
        return "redirect:/preptimes/show";
    }

}
