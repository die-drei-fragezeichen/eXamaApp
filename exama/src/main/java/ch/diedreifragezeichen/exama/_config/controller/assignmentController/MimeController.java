package ch.diedreifragezeichen.exama._config.controller.assignmentController;

import java.time.LocalDate;
import java.util.List;

//import javassist.NotFoundException;

import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import ch.diedreifragezeichen.exama.assignments.availablePrepTimes.AvailablePrepTime;
import ch.diedreifragezeichen.exama.assignments.availablePrepTimes.AvailablePrepTimeRepository;
import ch.diedreifragezeichen.exama.assignments.examTypes.ExamType;
import ch.diedreifragezeichen.exama.assignments.examTypes.ExamTypeRepository;
import ch.diedreifragezeichen.exama.assignments.mime.Mime;
import ch.diedreifragezeichen.exama.assignments.mime.MimeRepository;

@Controller
public class MimeController {
    @Autowired
    private MimeRepository mimeRepo;

    @Autowired
    private ExamTypeRepository examtypeRepo;

    @Autowired
    private AvailablePrepTimeRepository availablePrepTimeRepo;

   
    /**
     * The following methods handle the mime Creation
     */

    /**
     * user arriving at this path is enabled to create a new mime. When arriving at
     * the path a ModelAndView is created and the HTML link injected. New Mime
     * "template" is added to the ModelAndView and returned to the "HTML" to be
     * "filled"
     */

    @GetMapping("/mimes/create")
    public ModelAndView newMime() {
        Mime mime = new Mime();
        ModelAndView mav = new ModelAndView("teacherTemplates/mimesCreate");
        mav.addObject("verpacktesObjekt", mime);
        // NOTE: "verpacktesObjekt" is the so-called "NAME OF THE MODEL ATTRIBUTE"
        // mav.addAttribute("standardDate", new LocalDate());
        List<ExamType> listTypes = examtypeRepo.findAll();
        mav.addObject("holmirdietypenverpackung", listTypes);
        List<AvailablePrepTime> listPrepTimes = availablePrepTimeRepo.findAll();
        mav.addObject("bringmirdiepreptimes", listPrepTimes);

        return mav;
    }

    /**
     * When clicking the submit button, the User sends an Mime object. The mimeRepo
     * saves this object into the db note that the link here is different than in
     * the get method, but the user never really sees it user is redirected to the
     * "show" page immediately
     */

    @PostMapping("/mimes/created")
    public String processSaving(Mime mime) {
        mimeRepo.save(mime);
        return "redirect:/mimes/show";
    }

    /**
     * user arrives at exams/show and sees a list of all the mimes in DB table mimes.
     */

    @GetMapping("/mimes/show")
    public String showMimes(Model model) {
        List<Mime> listMimes = mimeRepo.findAll();
        model.addAttribute("nunNimmDieseListe", listMimes);
        return "teacherTemplates/mimesShow";
    }
}
