package ch.diedreifragezeichen.exama._config.controller.assignmentController;

import java.util.List;

//import javassist.NotFoundException;

import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import ch.diedreifragezeichen.exama.TESTKLASSEN.exams.ExamRepository;
import ch.diedreifragezeichen.exama.TESTKLASSEN.exams.Exam;

@Controller
public class ExamController {
    @Autowired
    private ExamRepository examRepo;


    /**
     * Exam Mappings
     */

    @GetMapping("/exams/show")
    public String showWorkloads(Model model){
        List<Exam> listWorkloads = examRepo.findAll();
        model.addAttribute("liste", listWorkloads);
        return "adminTemplates/examsShow";
    }



    @GetMapping("/exams/create")
    public ModelAndView newExam() {
        Exam exam = new Exam();
        ModelAndView mav = new ModelAndView("adminTemplates/examsCreate");
        mav.addObject("exam", exam);
        //mav.addAttribute("standardDate", new LocalDate());


        return mav;
    }

    @PostMapping("/exams/created")
    public String processSaving(Exam exam) {
        examRepo.save(exam);
        return "redirect:/exams/show";
    }

    // @GetMapping("/exams/{id}/edit")
    // public ModelAndView editexam(@PathVariable(name = "id") Long id) throws NotFoundException {
    //     Exam exam = examRepo.findExamById(id);
    //     if (exam == null) {
    //         throw new NotFoundException("Exam not found");
    //     }
    //     ModelAndView mav = new ModelAndView("adminTemplates/examEdit");
    //     mav.addObject("exam", exam);
    //     return mav;
    // }

    // @GetMapping("/exams/{id}/edited")
    // public String updateexam(@PathVariable(name = "id") Long id, @RequestParam(name = "name") String name,
    //         @RequestParam(name = "tag") String tag) throws NotFoundException {
    //     Exam exam = examRepo.findExamById(id);
    //     if (exam == null) {
    //         throw new NotFoundException("Exam not found");
    //     }
    //     examRepo.editexamById(id, name, tag);
    //     return "redirect:/exams/show";
    // }

    // @GetMapping("/exams/{id}/delete")
    // public String deleteexam(@PathVariable(name = "id") Long id) throws NotFoundException {
    //     Exam exam = examRepo.findExamById(id);
    //     if (exam == null) {
    //         throw new NotFoundException("Exam not found");
    //     }
    //     examRepo.deleteExamById(id);
    //     return "redirect:/exams/show";
    // }

}
