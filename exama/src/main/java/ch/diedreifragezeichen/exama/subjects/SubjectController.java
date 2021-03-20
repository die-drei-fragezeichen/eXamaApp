package ch.diedreifragezeichen.exama.subjects;

import java.util.*;

import javax.persistence.*;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class SubjectController {
    @Autowired
    private SubjectRepository subjectRepo;

    @PersistenceContext
    private EntityManager em;

    /**
     * Subject Mappings
     */

    @GetMapping("/subjects/show")
    public String show(Model model) {
        List<Subject> listSubjects = subjectRepo.findAll();
        model.addAttribute("allSubjects", listSubjects);
        return "adminTemplates/subjectsShow";
    }

    @GetMapping("/subjects/create")
    public ModelAndView add() {
        ModelAndView mav = new ModelAndView("adminTemplates/subjectModify");
        Subject subject = new Subject();
        mav.addObject("subject", subject);
        return mav;
    }

    @GetMapping("/subjects/edit")
    public ModelAndView edit(@RequestParam(name = "id") Long id) {
        ModelAndView mav = new ModelAndView("adminTemplates/subjectModify");
        Subject subject = subjectRepo.findSubjectById(id);
        mav.addObject("subject", subject);
        return mav;
    }

    @PostMapping("/subjects/modified")
    @Transactional
    public String modify(Subject subject) {
        em.unwrap(org.hibernate.Session.class).saveOrUpdate(subject);
        return "redirect:/subjects/show";
    }

    @GetMapping("/subjects/delete")
    public String delete(@RequestParam(name = "id") Long id) {
        subjectRepo.deleteById(id);
        return "redirect:/subjects/show";
    }
}