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
public class HolidayController {
    @Autowired
    private HolidayRepository holidayRepo;

    @Autowired
    private SemesterRepository semesterRepo;


    /**
     * Semester Mappings
     */

    @GetMapping("/holidays/create")
    public ModelAndView newHoliday() {
        
        ModelAndView mav = new ModelAndView("adminTemplates/holidayCreate");
        Holiday holiday = new Holiday();
        mav.addObject("newHoliday", holiday);
        List<Semester> allSemesters = semesterRepo.findAll();
        mav.addObject("allSemesters", allSemesters);
        return mav;
    }

    @PostMapping("/holidays/created")
    public String processSaving(Holiday holiday) {
        holidayRepo.save(holiday);
        return "redirect:/holidays/show";
    }

    @GetMapping("/holidays/show")
    public String listHolidays(Model model) {
        List<Holiday> listHolidays = holidayRepo.findAll();
        model.addAttribute("listHolidays", listHolidays);
        return "adminTemplates/holidaysShow";
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

