package ch.diedreifragezeichen.exama._landingControllers;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import ch.diedreifragezeichen.exama.assignments.availablePrepTimes.*;
import ch.diedreifragezeichen.exama.assignments.examTypes.*;
import ch.diedreifragezeichen.exama.assignments.exams.*;
import ch.diedreifragezeichen.exama.assignments.homeworks.Homework;
import ch.diedreifragezeichen.exama.assignments.workloadDistributions.*;
import ch.diedreifragezeichen.exama._services.AppService;
import ch.diedreifragezeichen.exama.assignments.*;
import ch.diedreifragezeichen.exama.assignments.assignment.Assignment;
import ch.diedreifragezeichen.exama.courses.*;
import ch.diedreifragezeichen.exama.operator.*;
import ch.diedreifragezeichen.exama.semesters.*;
import ch.diedreifragezeichen.exama.users.*;
import javassist.NotFoundException;

@Controller
public class teacherLanding {
    @Autowired
    private ExamRepository examRepo;
    @Autowired
    private ExamTypeRepository examtypeRepo;
    @Autowired
    private AvailablePrepTimeRepository availablePrepTimeRepo;
    @Autowired
    private CourseRepository courseRepo;
    @Autowired
    private UserRepository userRepo;
    @Autowired
    private WorkloadDistributionRepository distributionRepo;
    @Autowired
    private SemesterRepository semesterRepo;
    @Autowired
    AppService helper;

    @GetMapping("/teacher")
    public ModelAndView teacherLandingPage(Model model) throws NotFoundException {

        Authentication authLoggedInUser = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepo.findUserByEmail(authLoggedInUser.getName());
        ModelAndView mav = new ModelAndView("teacherTemplates/index");
        
        Semester currentSemester = helper.getCurrentSemesterBasedOnDate(LocalDate.now());
        Exam exam = new Exam();
        exam.setSemester(currentSemester);
        mav.addObject("exam", exam);

        Homework homework = new Homework();
        mav.addObject("homework", homework);

        List<Course> teacherStudentCourses = helper.getAllTeacherStudentCourses();
        mav.addObject("allCourses", teacherStudentCourses);

        List<ExamType> listTypes = examtypeRepo.findAll();
        mav.addObject("allExamTypes", listTypes);

        List<AvailablePrepTime> listPrepTimes = availablePrepTimeRepo.findAll();
        mav.addObject("allPrepTimes", listPrepTimes);

        List<WorkloadDistribution> listDist = distributionRepo.findAll();
        mav.addObject("allWorkloadDistributions", listDist);

        List<Assignment> allTeacherAssignments = helper.getAssignmentsForSevenDaysList(helper.getAllTeacherStudentCourses(), LocalDate.now().with(DayOfWeek.MONDAY));
        mav.addObject("assignments", allTeacherAssignments);

        return mav;
    }

}
