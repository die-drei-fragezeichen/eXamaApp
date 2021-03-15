package ch.diedreifragezeichen.exama._config.controller.assignmentController;

import java.util.List;

import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import ch.diedreifragezeichen.exama.assignments.examTypes.ExamType;
import ch.diedreifragezeichen.exama.assignments.examTypes.ExamTypeRepository;
import javassist.NotFoundException;

@Controller
public class ExamTypeController {
    @Autowired
    private ExamTypeRepository examTypeRepo;

    /**
     * ExamType Mappings
     */

    @GetMapping("/examtypes/create")
    public ModelAndView newExamType() {
        ExamType type = new ExamType();
        ModelAndView mav = new ModelAndView("adminTemplates/examtypeCreate");
        mav.addObject("type", type);
        return mav;
    }

    @PostMapping("/examtypes/created")
    public String processSaving(ExamType type) {
        examTypeRepo.save(type);
        return "redirect:/examtypes/show";
    }

    @GetMapping("/examtypes/show")
    public String listTypes(Model model) {
        List<ExamType> listTypes = examTypeRepo.findAll();
        model.addAttribute("listTypes", listTypes);
        return "adminTemplates/examtypesShow";
    }

    // @GetMapping("/examtypes/{id}/edit")
    // public ModelAndView editExamType(@PathVariable(name = "id") Long id) throws NotFoundException {
    //     ExamType type = examTypeRepo.getExamTypeByID(id);
    //     if (type == null) {
    //         throw new NotFoundException("ExamType not found");
    //     }
    //     ModelAndView mav = new ModelAndView("adminTemplates/examtypeEdit");
    //     mav.addObject("type", type);
    //     return mav;
    // }

    // @GetMapping("examtypes/{id}/edited")
    // public String updateExamType(@PathVariable(name = "id") Long id, @RequestParam(name = "name") String name) throws NotFoundException {
    //     ExamType type = examTypeRepo.getExamTypeByID(id);
    //     if (type == null) {
    //         throw new NotFoundException("ExamType not found");
    //     }
    //     examTypeRepo.editExamTypeById(id, name);
    //     return "redirect:/examtypes/show";
    // }

    // @GetMapping("examtypes/{id}/delete")
    // public String deleteExamType(@PathVariable(name = "id") Long id) throws NotFoundException {
    //     ExamType type = examTypeRepo.getExamTypeByID(id);
    //     if (type == null) {
    //         throw new NotFoundException("ExamType not found");
    //     }
    //     examTypeRepo.deleteExamTypeById(id);
    //     return "redirect:/examtypes/show";
    // }

}
