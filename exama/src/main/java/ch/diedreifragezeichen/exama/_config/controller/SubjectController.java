package ch.diedreifragezeichen.exama._config.controller;

import java.util.List;

import javassist.NotFoundException;

import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import ch.diedreifragezeichen.exama.subjects.Subject;
import ch.diedreifragezeichen.exama.subjects.SubjectRepository;

@Controller
public class SubjectController {
    @Autowired
    private SubjectRepository subjectRepo;

    /**
     * Subject Mappings
     */

    @GetMapping("/subjects/show")
    public String listSubjects(Model model) {
        List<Subject> listSubjects = subjectRepo.findAll();
        model.addAttribute("listSubjects", listSubjects);
        return "adminTemplates/subjectsShow";
    }

    @GetMapping("/subjects/create")
    public ModelAndView newSubject() {
        Subject subject = new Subject();
        ModelAndView mav = new ModelAndView("adminTemplates/subjectCreate");
        mav.addObject("subject", subject);
        return mav;
    }

    @PostMapping("/subjects/created")
    public String processSaving(Subject subject) {
        subjectRepo.save(subject);
        return "redirect:/subjects/show";
    }

    @GetMapping("/subjects/{id}/edit")
    public ModelAndView editSubject(@PathVariable(name = "id") Long id) throws NotFoundException {
        Subject subject = subjectRepo.getSubjectByID(id);
        if (subject == null) {
            throw new NotFoundException("Subject not found");
        }
        ModelAndView mav = new ModelAndView("adminTemplates/subjectEdit");
        mav.addObject("subject", subject);
        return mav;
    }

    @GetMapping("/subjects/{id}/edited")
    public String updateSubject(@PathVariable(name = "id") Long id, @RequestParam(name = "name") String name,
            @RequestParam(name = "tag") String tag) throws NotFoundException {
        Subject subject = subjectRepo.getSubjectByID(id);
        if (subject == null) {
            throw new NotFoundException("Subject not found");
        }
        subjectRepo.editSubjectById(id, name, tag);
        return "redirect:/subjects/show";
    }

    @GetMapping("/subjects/{id}/delete")
    public String deleteSubject(@PathVariable(name = "id") Long id) throws NotFoundException {
        Subject subject = subjectRepo.getSubjectByID(id);
        if (subject == null) {
            throw new NotFoundException("Subject not found");
        }
        subjectRepo.deleteSubjectById(id);
        return "redirect:/subjects/show";
    }

}
