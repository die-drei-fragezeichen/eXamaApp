package ch.diedreifragezeichen.exama._viewControllers;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import ch.diedreifragezeichen.exama.assignments.exams.Exam;
import ch.diedreifragezeichen.exama.assignments.exams.ExamRepository;
import ch.diedreifragezeichen.exama.assignments.homeworks.HomeworkRepository;
import ch.diedreifragezeichen.exama.courses.CoreCourse;
import ch.diedreifragezeichen.exama.courses.CoreCourseRepository;
import ch.diedreifragezeichen.exama.courses.Course;
import ch.diedreifragezeichen.exama.courses.CourseRepository;
import ch.diedreifragezeichen.exama.users.RoleRepository;
import ch.diedreifragezeichen.exama.users.User;
import ch.diedreifragezeichen.exama.users.UserRepository;

@Controller
public class weekAndSemesterViewController {
        @Autowired
        CoreCourseRepository coreCourseRepo;
        @Autowired
        CourseRepository courseRepo;
        @Autowired
        UserRepository userRepo;
        @Autowired
        RoleRepository roleRepo;
        @Autowired
        ExamRepository examRepo;
        @Autowired
        HomeworkRepository homeworkRepo;

        @GetMapping("/calendarchoose")
        public String calendarChoose(@RequestParam(name = "view") Long viewId,
                        @RequestParam(name = "coreCourse") Long coreCourseId) {

                // View 1: Workloaddiagram, View 2: Week View
                if (viewId == 1) {
                        LocalDate monday = LocalDate.now().with(DayOfWeek.MONDAY);
                        return "redirect:/calendar?view=" + viewId + "&monday=" + monday + "&coreCourse="
                                        + coreCourseId;
                }
                // View 3: Semester-View
                else if (viewId == 3) {
                        return "redirect:/semesterView/show?selectedSemester=1&selectedCoreCourse=" + coreCourseId;
                }
                return "redirect:/";
        }

        @GetMapping("/calendar")
        public ModelAndView workloadDiagram(@RequestParam(name = "view") Long viewId,
                        @RequestParam(name = "monday") String mondayString,
                        @RequestParam(name = "coreCourse") Long coreCourseId) {

                String currentUserName = SecurityContextHolder.getContext().getAuthentication().getName();
                User user = userRepo.findUserByEmail(currentUserName);
                List<CoreCourse> coreCourses = user.getCoreCourses();

                // if course is not existing -> redirect to home
                // security: if coreCourse is not assigned to user in any way -> redirect to
                // home (later errorpage)
                CoreCourse chosenCourse = coreCourseRepo.findCoreCourseById(coreCourseId);
                if (chosenCourse == null
                                || (user.getRoles().contains(roleRepo.findRoleByName("Teacher"))
                                                && !coreCourses.contains(chosenCourse))
                                || (user.getRoles().contains(roleRepo.findRoleByName("Student"))
                                                && !user.getCoreCourse().equals(chosenCourse))) {
                        return new ModelAndView("redirect:/");
                }

                List<Course> ccCourses = chosenCourse.getCourses();

                // Week View
                if (viewId == 2) {
                        ModelAndView mav = new ModelAndView("teacherTemplates/weeklyView.html");
                        mav.addObject("coreCourse", coreCourseRepo.findCoreCourseById(coreCourseId));
                        LocalDate monday = LocalDate.parse(mondayString);
                        mav.addObject("monday", monday);
                        mav.addObject("nextMonday", monday.plusWeeks(1));
                        mav.addObject("lastMonday", monday.minusWeeks(1));

                        List<Exam> allExams = ccCourses.stream().filter(c -> Objects.nonNull(c.getExams()))
                                        .map(c -> c.getExams()).flatMap(List::stream).distinct()
                                        .collect(Collectors.toList());
                        List<Exam> exams = allExams.stream().filter(c -> c.getDueDate().isBefore(monday.plusDays(7)))
                                        .filter(c -> c.getDueDate().isAfter(monday.minusDays(1)))
                                        .collect(Collectors.toList());
                        mav.addObject("exams", exams);

                        return mav;
                }
                // Workload Diagram
                else {

                        ModelAndView mav = new ModelAndView("teacherTemplates/workloadDiagram.html");
                        mav.addObject("coreCourse", coreCourseRepo.findCoreCourseById(coreCourseId));
                        LocalDate monday = LocalDate.parse(mondayString);
                        mav.addObject("monday", monday);
                        mav.addObject("nextMonday", monday.plusWeeks(1));
                        mav.addObject("lastMonday", monday.minusWeeks(1));

                        List<Exam> allExams = ccCourses.stream().filter(c -> Objects.nonNull(c.getExams()))
                                        .map(c -> c.getExams()).flatMap(List::stream).distinct()
                                        .collect(Collectors.toList());
                        List<Exam> exams = allExams.stream().filter(c -> c.getDueDate().isAfter(monday.minusDays(1)))
                                        .collect(Collectors.toList()); //

                        Double[] workloadTotalDaysList = { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0 };

                        if (exams != null) {
                                List<Double> workloadListMonday = exams.stream().map(c -> c.getWorkloadValue(monday))
                                                .collect(Collectors.toList());
                                workloadTotalDaysList[0] = Math
                                                .min(workloadListMonday.stream().mapToDouble(w -> w).sum(), 1);

                                List<Double> workloadListTuesday = exams.stream()
                                                .map(c -> c.getWorkloadValue(monday.plusDays(1)))
                                                .collect(Collectors.toList());
                                workloadTotalDaysList[1] = Math
                                                .min(workloadListTuesday.stream().mapToDouble(w -> w).sum(), 1);

                                List<Double> workloadListWednesday = exams.stream()
                                                .map(c -> c.getWorkloadValue(monday.plusDays(2)))
                                                .collect(Collectors.toList());
                                workloadTotalDaysList[2] = Math
                                                .min(workloadListWednesday.stream().mapToDouble(w -> w).sum(), 1);

                                List<Double> workloadListThursday = exams.stream()
                                                .map(c -> c.getWorkloadValue(monday.plusDays(3)))
                                                .collect(Collectors.toList());
                                workloadTotalDaysList[3] = Math
                                                .min(workloadListThursday.stream().mapToDouble(w -> w).sum(), 1);

                                List<Double> workloadListFriday = exams.stream()
                                                .map(c -> c.getWorkloadValue(monday.plusDays(4)))
                                                .collect(Collectors.toList());
                                workloadTotalDaysList[4] = Math
                                                .min(workloadListFriday.stream().mapToDouble(w -> w).sum(), 1);

                                List<Double> workloadListSaturday = exams.stream()
                                                .map(c -> c.getWorkloadValue(monday.plusDays(5)))
                                                .collect(Collectors.toList());
                                workloadTotalDaysList[5] = Math
                                                .min(workloadListSaturday.stream().mapToDouble(w -> w).sum(), 1);

                                List<Double> workloadListSunday = exams.stream()
                                                .map(c -> c.getWorkloadValue(monday.plusDays(6)))
                                                .collect(Collectors.toList());
                                workloadTotalDaysList[6] = Math
                                                .min(workloadListSunday.stream().mapToDouble(w -> w).sum(), 1);
                        }
                        mav.addObject("workloadValueList", workloadTotalDaysList);

                        return mav;
                }
        }
}
