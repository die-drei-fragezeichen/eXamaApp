package ch.diedreifragezeichen.exama._config.controller;

import java.util.*;

import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ch.diedreifragezeichen.exama.semesters.Holiday;
import ch.diedreifragezeichen.exama.semesters.HolidayRepository;
import ch.diedreifragezeichen.exama.semesters.Semester;
import ch.diedreifragezeichen.exama.semesters.SemesterRepository;


@Controller
public class SemesterController {
    @Autowired
    private SemesterRepository semesterRepo;

    @Autowired
    private HolidayRepository holidayRepo;


    /**
     * Semester Mappings
     */

    @GetMapping("/semesters/create")
    public ModelAndView newSubject() {
        
        ModelAndView mav = new ModelAndView("adminTemplates/semesterCreate");
        Semester semester = new Semester();
        mav.addObject("newSemester", semester);
        List<Holiday> allHolidays = holidayRepo.findAll();
        mav.addObject("allHolidays", allHolidays);
        return mav;
    }

    @PostMapping("/semesters/created")
    public String processSaving(Semester semester) {
        semesterRepo.save(semester);
        return "redirect:/semesters/show";
    }

    @GetMapping("/semesters/show")
    public String listSemesters(Model model) {
        List<Semester> listSemesters = semesterRepo.findAll();
        model.addAttribute("allSemesters", listSemesters);
        return "adminTemplates/semestersShow";
    }

    // @GetMapping("/subjects/{id}/edit")
    // public ModelAndView editSubject(@PathVariable(name = "id") Long id) throws NotFoundException {
    //     Subject subject = subjectRepo.findSubjectById(id);
    //     if (subject == null) {
    //         throw new NotFoundException("Subject not found");
    //     }
    //     ModelAndView mav = new ModelAndView("adminTemplates/subjectEdit");
    //     mav.addObject("subject", subject);
    //     return mav;
    // }

    // @GetMapping("/subjects/{id}/edited")
    // public String updateSubject(@PathVariable(name = "id") Long id, @RequestParam(name = "name") String name,
    //         @RequestParam(name = "tag") String tag) throws NotFoundException {
    //     Subject subject = subjectRepo.findSubjectById(id);
    //     if (subject == null) {
    //         throw new NotFoundException("Subject not found");
    //     }
    //     subjectRepo.editSubjectById(id, name, tag);
    //     return "redirect:/subjects/show";
    // }

    // @GetMapping("/subjects/{id}/delete")
    // public String deleteSubject(@PathVariable(name = "id") Long id) throws NotFoundException {
    //     Subject subject = subjectRepo.findSubjectById(id);
    //     if (subject == null) {
    //         throw new NotFoundException("Subject not found");
    //     }
    //     subjectRepo.deleteSubjectById(id);
    //     return "redirect:/subjects/show";
    // }

}

