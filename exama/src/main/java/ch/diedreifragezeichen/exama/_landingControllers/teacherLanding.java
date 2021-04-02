package ch.diedreifragezeichen.exama._landingControllers;

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
import ch.diedreifragezeichen.exama.assignments.*;
import ch.diedreifragezeichen.exama.courses.*;
import ch.diedreifragezeichen.exama.operator.*;
import ch.diedreifragezeichen.exama.semesters.*;
import ch.diedreifragezeichen.exama.users.*;

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
    private OperatorRepository operatorRepo;
    @Autowired
    private SemesterRepository semesterRepo;

    
    @GetMapping("/teacher")
    public ModelAndView teacherLandingPage(Model model) {

        Authentication authLoggedInUser = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepo.findUserByEmail(authLoggedInUser.getName());
        ModelAndView mav = new ModelAndView("teacherTemplates/index");

        Exam exam = new Exam();
        mav.addObject("exam", exam);

        
        Homework homework = new Homework();
        mav.addObject("homework", homework);

        Semester semester = semesterRepo.findAll().stream().filter(s -> s.getStartDate().isBefore(LocalDate.now()) && s.getEndDate().isAfter(LocalDate.now())).collect(Collectors.toList()).get(0);
        exam.setSemester(semester);

        List<Course> listCourses = courseRepo.findAll();
        List<Course> usersCourses = listCourses.stream().filter(c -> c.getUsers().contains(user))
                .collect(Collectors.toList());
        mav.addObject("allCourses", usersCourses);

        List<ExamType> listTypes = examtypeRepo.findAll();
        mav.addObject("allExamTypes", listTypes);

        List<AvailablePrepTime> listPrepTimes = availablePrepTimeRepo.findAll();
        mav.addObject("allPrepTimes", listPrepTimes);

        List<WorkloadDistribution> listDist = distributionRepo.findAll();
        mav.addObject("allWorkloadDistributions", listDist);

        return mav;
    }
    
}
