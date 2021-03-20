package ch.diedreifragezeichen.exama._config.controller;

import java.util.*;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

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

    @PersistenceContext
    private EntityManager em;


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
        //Sort the list by StartDate for nice display
        listHolidays.sort(Comparator.comparing(Holiday::getStartDate));
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
        em.unwrap(org.hibernate.Session.class).saveOrUpdate(holiday);
        return "redirect:/holidays/show";
    }

    @GetMapping("/holidays/delete")
    public String deleteHoliday(@RequestParam(name = "id") Long id) {
        holidayRepo.deleteById(id);
        return "redirect:/holidays/show";
    }
}

