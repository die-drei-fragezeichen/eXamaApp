package ch.diedreifragezeichen.exama.courses;

import java.util.List;

import javax.persistence.*;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ch.diedreifragezeichen.exama.subjects.*;

@Controller
public class CourseController {
    @Autowired
    private CourseRepository courseRepo;

    @Autowired
    private SubjectRepository subjectRepo;

    @PersistenceContext
    private EntityManager em;

    /**
     * Course Mappings
     */

    @GetMapping("/courses/show")
    public String showCourses(Model model) {
        List<Course> listCourses = courseRepo.findAll();
        model.addAttribute("allCourses", listCourses);
        return "adminTemplates/coursesShow";
    }

    @GetMapping("/courses/create")
    public ModelAndView newCoreCourse() {
        ModelAndView mav = new ModelAndView("adminTemplates/courseModify");
        Course newCourse = new Course();
        mav.addObject("course", newCourse);
        List<Subject> subjectList = subjectRepo.findAll();
        mav.addObject("allSubjects", subjectList);
        return mav;
    }

    @GetMapping("/courses/edit")
    public ModelAndView updateCourse(@RequestParam(name = "id") Long id) {
        ModelAndView mav = new ModelAndView("adminTemplates/courseModify");
        Course course = courseRepo.findCourseById(id);
        mav.addObject("course", course);
        List<Subject> subjectList = subjectRepo.findAll();
        mav.addObject("allSubjects", subjectList);
        return mav;
    }

    @PostMapping("/courses/modified")
    @Transactional
    public String modify(Course course) {
        em.unwrap(org.hibernate.Session.class).saveOrUpdate(course);
        return "redirect:/courses/show";
    }

    @GetMapping("/courses/delete")
    public String deleteCourse(@RequestParam(name = "id") Long id) {
        courseRepo.deleteById(id);
        return "redirect:/courses/show";
    }
}
