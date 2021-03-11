package ch.diedreifragezeichen.exama._config.controller;

import java.util.List;

import javassist.NotFoundException;

import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ch.diedreifragezeichen.exama.courses.*;

@Controller
public class CourseController {
    @Autowired
    private CourseRepository courseRepo;

    /**
     * Course Mappings
     */

    @GetMapping("/courses/create")
    public ModelAndView newCourse() {
        Course course = new Course();
        ModelAndView mav = new ModelAndView("adminTemplates/courseCreate");
        mav.addObject("course", course);
        return mav;
    }

    @PostMapping("/courses/created")
    public String processSaving(Course course) {
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
        mav.addObject("Course", Course);
        return mav;
    }
    /*
     * unimplemented parts
     */

    // @GetMapping("/Courses/{id}/edit")
    // public ModelAndView editCourse(@PathVariable(name = "id") Long id) throws
    // NotFoundException {
    // Course Course = courseRepo.findCourseById(id);
    // if (Course == null) {
    // throw new NotFoundException("Course not found");
    // }
    // ModelAndView mav = new ModelAndView("adminTemplates/CourseEdit");
    // mav.addObject("Course", Course);
    // return mav;
    // }

    // @GetMapping("/Courses/{id}/edited")
    // public String updateCourse(@PathVariable(name = "id") Long id,
    // @RequestParam(name = "name") String name,
    // @RequestParam(name = "tag") String tag) throws NotFoundException {
    // Course Course = courseRepo.findCourseById(id);
    // if (Course == null) {
    // throw new NotFoundException("Course not found");
    // }
    // courseRepo.editCourseById(id, name, tag);
    // return "redirect:/Courses/show";
    // }

    // @GetMapping("/Courses/{id}/delete")
    // public String deleteCourse(@PathVariable(name = "id") Long id) throws
    // NotFoundException {
    // Course Course = courseRepo.findCourseById(id);
    // if (Course == null) {
    // throw new NotFoundException("Course not found");
    // }
    // courseRepo.deleteCourseById(id);
    // return "redirect:/Courses/show";
    // }

}
