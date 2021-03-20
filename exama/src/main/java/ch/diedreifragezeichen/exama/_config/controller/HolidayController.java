package ch.diedreifragezeichen.exama._config.controller;

import java.util.*;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;


import ch.diedreifragezeichen.exama.semesters.Holiday;
import ch.diedreifragezeichen.exama.semesters.HolidayRepository;
import ch.diedreifragezeichen.exama.semesters.HolidayService;
import ch.diedreifragezeichen.exama.semesters.Semester;
import ch.diedreifragezeichen.exama.semesters.SemesterRepository;

@Controller
public class HolidayController {
    @Autowired
    private HolidayService holidayService;
    @Autowired
    private HolidayRepository holidayRepo;
    @Autowired
    private SemesterRepository semesterRepo;


    /**
     * Semester Mappings
     */

    @GetMapping("/holidays/create")
    public ModelAndView newHoliday() {
        ModelAndView mav = new ModelAndView("adminTemplates/holidayModify");
        
        Holiday holiday = new Holiday();
        mav.addObject("holiday", holiday);
        
        List<Semester> allSemesters = semesterRepo.findAll().stream().filter(s->s.isEnabled()).collect(Collectors.toList());
        mav.addObject("allSemesters", allSemesters);
        return mav;
    }
    
    @GetMapping("/holidays/show")
    public String showHolidays(Model model) {
        List<Holiday> listHolidays = holidayRepo.findAll();
        model.addAttribute("listHolidays", listHolidays);
        return "adminTemplates/holidaysShow";
    }

    
    @GetMapping("/holidays/edit")
    public ModelAndView updateHoliday(@RequestParam(name = "id") Long id) {
        ModelAndView mav = new ModelAndView("adminTemplates/holidayModify");
        //fetch Holiday you want to edit from Database
        Holiday holiday = holidayRepo.findHolidayById(id);
        mav.addObject("holiday", holiday);
        List<Semester> allSemesters = semesterRepo.findAll().stream().filter(s->s.isEnabled()).collect(Collectors.toList());
        mav.addObject("allSemesters", allSemesters);

        return mav;
    }
    
    @PostMapping("/holidays/modified")
    @Transactional
    public String modifyHoliday(Holiday holiday) {
        holidayService.saveOrUpdateHoliday(holiday);
        return "redirect:/holidays/show";
    }

    @GetMapping("/holidays/delete")
    public String deleteHoliday(@RequestParam(name = "id") Long id) {
        holidayRepo.deleteById(id);
        return "redirect:/holidays/show";
    }
}

