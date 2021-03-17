package ch.diedreifragezeichen.exama._config.controller;

import java.util.List;

import javassist.NotFoundException;

import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ch.diedreifragezeichen.exama.courses.*;
import ch.diedreifragezeichen.exama.subjects.Subject;
import ch.diedreifragezeichen.exama.subjects.SubjectRepository;

@Controller
public class CourseController {
    @Autowired
    private CourseRepository courseRepo;

    @Autowired
    private SubjectRepository subjectRepo;

    /**
     * Course Mappings
     */

    @GetMapping("/courses/create")
    public ModelAndView newCourse() {
        Course course = new Course();
        ModelAndView mav = new ModelAndView("adminTemplates/courseCreate");
        mav.addObject("course", course);
        List<Subject> subjectList = subjectRepo.findAll();
        mav.addObject("allSubjects", subjectList);
        return mav;
    }

    @PostMapping("/courses/created")
    public String processSaving(Course course) {
        course.setEnabled(true);
        courseRepo.save(course);
        return "redirect:/courses/show";
    }

    @GetMapping("/courses/show")
    public String showCourses(Model model) {
        List<Course> listCourses = courseRepo.findAll();
        model.addAttribute("listCourses", listCourses);
        return "adminTemplates/coursesShow";
    }

    @GetMapping("/courses/{id}/edit")
    public ModelAndView editCourse(@PathVariable(name = "id") Long id) throws NotFoundException {
        Course Course = courseRepo.findCourseById(id);
        if (Course == null) {
            throw new NotFoundException("Die Klasse existiert leider noch nicht");
        }
        ModelAndView mav = new ModelAndView("adminTemplates/courseEdit");
        mav.addObject("course", Course);
        return mav;
    }

    @GetMapping("/course/{id}/edited")
    public String updateUser(@PathVariable(name = "id") long id,
            @RequestParam(name = "name") String name, @RequestParam(name = "enabled", required = false) boolean enabled) throws NotFoundException {
        Course course = courseRepo.findCourseById(id);
        if (course == null) {
            throw new NotFoundException("Course not found");
        }
        return "redirect:/courses/show";
    }
}
