package ch.diedreifragezeichen.exama.assignments.examTypes;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ExamTypeController {
    @Autowired
    private ExamTypeRepository examTypeRepo;

    @PersistenceContext
    private EntityManager em;

    /**
     * ExamType Mappings
     */

    @GetMapping("/examtypes/show")
    public String show(Model model) {
        List<ExamType> listTypes = examTypeRepo.findAll();
        model.addAttribute("allExamTypes", listTypes);
        return "adminTemplates/examtypesShow";
    }

    @GetMapping("/examtypes/create")
    public ModelAndView add() {
        ModelAndView mav = new ModelAndView("adminTemplates/examtypeModify");
        ExamType examType = new ExamType();
        mav.addObject("examType", examType);
        return mav;
    }

    @GetMapping("/examtypes/edit")
    public ModelAndView edit(@RequestParam(name = "id") Long id) {
        ModelAndView mav = new ModelAndView("adminTemplates/examtypeModify");
        ExamType examType = examTypeRepo.findExamTypeById(id);
        mav.addObject("examType", examType);
        return mav;
    }

    @PostMapping("/examtypes/modified")
    @Transactional
    public String modify(ExamType examType) {
        em.unwrap(org.hibernate.Session.class).saveOrUpdate(examType);
        return "redirect:/examtypes/show";
    }

    @GetMapping("/examtypes/delete")
    public String delete(@RequestParam(name = "id") Long id) {
        examTypeRepo.deleteById(id);
        return "redirect:/examtypes/show";
    }
}