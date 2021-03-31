// package ch.diedreifragezeichen.exama._viewControllers;

// import java.time.DayOfWeek;
// import java.time.LocalDate;
// import java.util.List;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.stereotype.Controller;
// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.RequestParam;
// import org.springframework.web.servlet.ModelAndView;

// import ch.diedreifragezeichen.exama._services.AppService;
// import ch.diedreifragezeichen.exama.assignments.assignment.Assignment;
// import ch.diedreifragezeichen.exama.assignments.exams.Exam;
// import ch.diedreifragezeichen.exama.courses.CoreCourse;
// import ch.diedreifragezeichen.exama.courses.CoreCourseRepository;
// import ch.diedreifragezeichen.exama.courses.Course;
// import ch.diedreifragezeichen.exama.users.User;


// @Controller
// public class weekAndSemesterViewControllerOld {
//         @Autowired
//         CoreCourseRepository coreCourseRepo;

//         @Autowired
//         AppService helper;

//         @GetMapping("/calendarchoose")
//         public String calendarChoose(@RequestParam(name = "view") Long viewId,
//                         @RequestParam(name = "coreCourse") Long coreCourseId) {

//                 // View 1: Direct User to Workloaddiagram
//                 if (viewId == 1) {
//                         LocalDate monday = LocalDate.now().with(DayOfWeek.MONDAY);
//                         return "redirect:/calendar?view=" + viewId + "&monday=" + monday + "&coreCourse="
//                                         + coreCourseId;
//                 }
//                 // View 3: Direct User to SemesterView
//                 else if (viewId == 3) {
//                         return "redirect:/semesterView/show?selectedSemester=1&selectedCoreCourse=" + coreCourseId;
//                 }
//                 return "redirect:/";
//         }

//         @GetMapping("/calendar")
//         public ModelAndView workloadDiagram(@RequestParam(name = "view") Long viewId,
//                         @RequestParam(name = "monday") String mondayString,
//                         @RequestParam(name = "coreCourse") Long coreCourseId) {
//                 // retrieve List of coreCourses form current user
//                 User user = helper.getCurrentUser();
//                 List<CoreCourse> userCoreCourses = user.getCoreCourses();
//                 CoreCourse selectedCourse = coreCourseRepo.findCoreCourseById(coreCourseId);
//                 // if chosen CoreCourse empty -> redirect to home
//                 // (security check) if coreCourse is not assigned to user in any way -> redirect
//                 // to home (TODO: later errorpage)
//                 if (selectedCourse == null
//                                 || (helper.currentUserIsA("Student") && !user.getCoreCourse().equals(selectedCourse))
//                                 || (helper.currentUserIsA("Teacher") && !userCoreCourses.contains(selectedCourse))) {
//                         return new ModelAndView("redirect:/");
//                 }

//                 List<Course> selectedCourses = selectedCourse.getCourses();
//                 final LocalDate monday = helper.getLocaldateFromString(mondayString);
//                 ModelAndView mav;

//                 // View 2: Direct User to WeekView
//                 if (viewId == 2) {
//                         mav = new ModelAndView("teacherTemplates/weeklyView.html");
//                         mav.addObject("coreCourse", selectedCourse);

//                         mav.addObject("monday", monday);
//                         mav.addObject("nextMonday", monday.plusWeeks(1));
//                         mav.addObject("lastMonday", monday.minusWeeks(1));

//                         // add all assignments for weekView
//                         List<Assignment> assignments = helper.getAssignmentsForSevenDaysList(selectedCourses, monday);
//                         // add all assignments for weekView
//                         mav.addObject("assignments", assignments);

//                         // add exams for examBar
//                         List<Exam> exams = helper.getExamsForSevenDaysList(selectedCourses, monday);
//                         mav.addObject("allWeekExams", exams);
//                         return mav;
//                 }
//                 // else show Workload Diagram
//                 else {

//                         mav = new ModelAndView("teacherTemplates/workloadDiagram.html");
//                         mav.addObject("coreCourse", selectedCourse);
//                         mav.addObject("monday", monday);
//                         mav.addObject("nextMonday", monday.plusWeeks(1));
//                         mav.addObject("lastMonday", monday.minusWeeks(1));
//                         // add all exams for two weeks so two weeks can be displayed
//                         List<Exam> examsThisWeek = helper.getExamsForSevenDaysList(selectedCourses, monday);
//                         mav.addObject("examsThisWeek", examsThisWeek);
//                         List<Exam> examsNextWeek = helper.getExamsForSevenDaysList(selectedCourses, monday.plusDays(7));
//                         mav.addObject("examsNextWeek", examsNextWeek);

//                         // uses Hilfsmethode 2 below - calculates how much the actual exams "count"
//                         mav.addObject("xFactor", helper.calculateExamFactor(examsThisWeek));

//                         Double[] workloadTotalDaysList = helper.getWorkloadValueForSevenDaysArray(coreCourseId, monday);
//                         mav.addObject("workloadValueList", workloadTotalDaysList);

//                         return mav;
//                 }
//         }
// }
