package ch.diedreifragezeichen.exama._viewControllers;

import java.time.*;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import ch.diedreifragezeichen.exama._services.AppService;
import ch.diedreifragezeichen.exama.assignments.assignment.Assignment;
import ch.diedreifragezeichen.exama.assignments.exams.Exam;
import ch.diedreifragezeichen.exama.courses.*;
import ch.diedreifragezeichen.exama.users.User;

@Controller
public class weekAndSemesterViewController {
        @Autowired
        CoreCourseRepository coreCourseRepo;
        @Autowired
        AppService helper;

        @GetMapping("/calendarchoose")
        public String calendarChoose(@RequestParam(name = "view") Long viewId,
                        @RequestParam(name = "coreCourse") Long coreCourseId) {

                if (viewId == 1) {// if viewId = 1: redirect User to Workloaddiagram

                        LocalDate monday = LocalDate.now().with(DayOfWeek.MONDAY);
                        return "redirect:/calendar?view=" + viewId + "&monday=" + monday + "&coreCourse="
                                        + coreCourseId;
                } else if (viewId == 3) { // if viewID = 3: redirect User to SemesterView
                        return "redirect:/semesterView/show?selectedSemester=1&selectedCoreCourse=" + coreCourseId;
                }
                return "redirect:/";
        }

        @GetMapping("/calendar")
        public ModelAndView workloadDiagram(@RequestParam(name = "view") Long viewId,
                        @RequestParam(name = "monday") String mondayString,
                        @RequestParam(name = "coreCourse") Long coreCourseId) {
                // retrieve List of coreCourses form current user
                User user = helper.getCurrentUser();
                List<CoreCourse> userCoreCourses = user.getCoreCourses();
                CoreCourse selectedCourse = coreCourseRepo.findCoreCourseById(coreCourseId);
                // if chosen CoreCourse empty -> redirect to home
                // (security check) if coreCourse is not assigned to user in any way -> redirect
                // to home (TODO: later errorpage)
                if (selectedCourse == null
                                || (helper.currentUserIsA("Student") && !user.getCoreCourse().equals(selectedCourse))
                                || (helper.currentUserIsA("Teacher") && !userCoreCourses.contains(selectedCourse))) {
                        return new ModelAndView("redirect:/");
                }

                List<Course> selectedCourses = selectedCourse.getCourses();
                final LocalDate monday = helper.getLocaldateFromString(mondayString);

                ModelAndView mav;
                if (viewId == 2) {
                        // direct to weeklyView
                        mav = new ModelAndView("teacherTemplates/weeklyView.html");
                } else {// direct to workloadDiagram
                        mav = new ModelAndView("teacherTemplates/workloadDiagram.html");
                }
                mav.addObject("coreCourse", selectedCourse);
                // add all necessary dates
                mav.addObject("monday", monday);
                mav.addObject("nextMonday", monday.plusWeeks(1));
                mav.addObject("lastMonday", monday.minusWeeks(1));

                if (viewId == 2) {
                        // add all ASSIGNMENTS for ONE weekView
                        List<Assignment> assignments = helper.getAssignmentsForSevenDaysList(selectedCourses, monday);
                        mav.addObject("assignments", assignments);
                        // add ALL EXAMS for examBar
                        List<Exam> exams = helper.getExamsForSevenDaysList(selectedCourses, monday);
                        mav.addObject("allWeekExams", exams);

                } else {
                        // For WORKLOAD DIAGRAM add all workload, the eXam Factor and Exams for two
                        // weeks (all of which will be displayed)
                        Double[] workloadTotalDaysList = helper.getWorkloadValueForSevenDaysArray(coreCourseId, monday);
                        mav.addObject("workloadValueList", workloadTotalDaysList);

                        List<Exam> examsThisWeek = helper.getExamsForSevenDaysList(selectedCourses, monday);
                        mav.addObject("examsThisWeek", examsThisWeek);
                        List<Exam> examsNextWeek = helper.getExamsForSevenDaysList(selectedCourses, monday.plusDays(7));
                        mav.addObject("examsNextWeek", examsNextWeek);

                        // add the exam factor (i.e., exam count for ONE week based on counting Factor)
                        mav.addObject("xFactor", helper.calculateExamFactor(examsThisWeek));
                }
                return mav;
        }
}
