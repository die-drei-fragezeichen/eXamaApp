package ch.diedreifragezeichen.exama._viewControllers;

import java.time.DayOfWeek;
import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import ch.diedreifragezeichen.exama.courses.CoreCourseRepository;

@Controller
public class workloadDiagramController {
    @Autowired
    CoreCourseRepository coreCourseRepo;
    
    @GetMapping("/calendarchoose")
    public String calendarChoose(@RequestParam(name = "view") Long viewId, @RequestParam(name = "coreCourse") Long coreCourseId) {
        
        //View 1: Workloaddiagram, View 2: Week View
        if(viewId == 1){
            LocalDate monday = LocalDate.now().with(DayOfWeek.MONDAY);
            return "redirect:/calendar?view="+viewId+"&monday="+monday+"&coreCourse="+coreCourseId;
        }
        //View 3: Semester-View
        else if(viewId == 3){
            return "redirect:/semesterView/show?selectedSemester=1&selectedCoreCourse="+coreCourseId;
        }
        return "redirect:/";
    }

    @GetMapping("/calendar")
    public ModelAndView workloadDiagram(@RequestParam(name = "view") Long viewId, @RequestParam(name = "monday") String mondayString, @RequestParam(name = "coreCourse") Long coreCourseId) {
        
        //Week View
        if(viewId==2){
            ModelAndView mav = new ModelAndView("teacherTemplates/weeklyView.html");
            mav.addObject("coreCourse", coreCourseRepo.findCoreCourseById(coreCourseId));
            LocalDate monday = LocalDate.parse(mondayString);
            mav.addObject("monday", monday);
            return mav;
        }
        //Workload Diagram
        else{
            ModelAndView mav = new ModelAndView("teacherTemplates/workloadDiagram.html");
            mav.addObject("coreCourse", coreCourseRepo.findCoreCourseById(coreCourseId));
            LocalDate monday = LocalDate.parse(mondayString);
            mav.addObject("monday", monday);
            return mav;
        }
    }
}
