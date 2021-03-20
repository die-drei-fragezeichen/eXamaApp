package ch.diedreifragezeichen.exama.semesters;

import java.util.*;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class SemesterController {
    @Autowired
    private SemesterRepository semesterRepo;

    @Autowired
    private HolidayRepository holidayRepo;

    @PersistenceContext
    private EntityManager em;

    /**
     * Semester Mappings
     */

    @GetMapping("/semesters/create")
    public ModelAndView newSubject() {

        ModelAndView mav = new ModelAndView("adminTemplates/semesterModify");
        Semester semester = new Semester();
        mav.addObject("semester", semester);

        List<Holiday> allHolidays = holidayRepo.findAll();
        mav.addObject("allHolidays", allHolidays);
        return mav;
    }

    @GetMapping("/semesters/show")
    public String listSemesters(Model model) {
        List<Semester> listSemesters = semesterRepo.findAll();
        //Sort the list by StartDate for nice display
        listSemesters.sort(Comparator.comparing(Semester::getStartDate));
        model.addAttribute("allSemesters", listSemesters);
        return "adminTemplates/semestersShow";
    }

    @GetMapping("/semesters/edit")
    public ModelAndView updateSemester(@RequestParam(name = "id") Long id) {
        ModelAndView mav = new ModelAndView("adminTemplates/semesterModify");
        // fetch the Semester you want to edit from Database
        Semester semester = semesterRepo.findSemesterById(id);
        mav.addObject("semester", semester);
        List<Holiday> allHolidays = holidayRepo.findAll();
        mav.addObject("allHolidays", allHolidays);

        return mav;
    }

    @PostMapping("/semesters/modified")
    @Transactional
    public String modifySemester(Semester semester) {
        em.unwrap(org.hibernate.Session.class).saveOrUpdate(semester);
        return "redirect:/semesters/show";
    }

    @GetMapping("/semesters/delete")
    public String deleteSemester(@RequestParam(name = "id") Long id) {
        semesterRepo.deleteById(id);
        return "redirect:/holidays/show";
    }

}
