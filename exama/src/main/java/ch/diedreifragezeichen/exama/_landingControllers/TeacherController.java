package ch.diedreifragezeichen.exama._landingControllers;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import ch.diedreifragezeichen.exama.assignments.availablePrepTimes.*;
import ch.diedreifragezeichen.exama.assignments.examTypes.*;
import ch.diedreifragezeichen.exama.assignments.exams.*;
import ch.diedreifragezeichen.exama.assignments.homeworks.Homework;
import ch.diedreifragezeichen.exama.assignments.workloadDistributions.*;
import ch.diedreifragezeichen.exama._services.AppService;
import ch.diedreifragezeichen.exama.assignments.assignment.Assignment;
import ch.diedreifragezeichen.exama.courses.*;
import ch.diedreifragezeichen.exama.semesters.*;
import javassist.NotFoundException;

@Controller
public class TeacherController {
    @Autowired
    private ExamTypeRepository examtypeRepo;
    @Autowired
    private AvailablePrepTimeRepository availablePrepTimeRepo;
    @Autowired
    private WorkloadDistributionRepository distributionRepo;
    @Autowired
    AppService helper;

    @GetMapping("/teacher")
    public ModelAndView teacherLandingPage(@RequestParam(name = "day", required = false) String dayString)
            throws NotFoundException {
        ModelAndView mav = new ModelAndView("teacherTemplates/index");

        LocalDate day = LocalDate.now().with(DayOfWeek.MONDAY);
        LocalDate monday = day;
        if (dayString != null && !dayString.equals("")) {
            day = helper.getLocaldateFromString(dayString);
            monday = day.with(DayOfWeek.MONDAY);
        }

        mav.addObject("monday", monday);
        mav.addObject("nextMonday", monday.plusWeeks(1));
        mav.addObject("lastMonday", monday.minusWeeks(1));

        Semester currentSemester = helper.getCurrentSemesterBasedOnDate(day);
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

        List<Assignment> allTeacherAssignmentsWeek = helper
                .getAssignmentsForSevenDaysList(helper.getAllTeacherStudentCourses(), monday);
        mav.addObject("assignments", allTeacherAssignmentsWeek);

        return mav;
    }

    @GetMapping("/teacher/semester-overview")
    public ModelAndView teacherSemesterOverview(@RequestParam(name = "day", required = false) String dayString)
            throws NotFoundException {
        ModelAndView mav = new ModelAndView("teacherTemplates/semesterOverview");

        LocalDate day = LocalDate.now().with(DayOfWeek.MONDAY);
        if (dayString != null && !dayString.equals("")) {
            day = helper.getLocaldateFromString(dayString);
        }

        Semester semester = helper.getCurrentSemesterBasedOnDate(day);
        Semester prevSemester = null; // helper.getCurrentSemesterBasedOnDate(chosenSemester.getStartDate().minusDays(60));
        if (prevSemester == null) {
            mav.addObject("prevStartDate", null);
        } else {
            mav.addObject("prevStartDate", prevSemester.getStartDate());
        }
        Semester nextSemester = null; // helper.getCurrentSemesterBasedOnDate(chosenSemester.getEndDate().plusDays(60));
        if (nextSemester == null) {
            mav.addObject("nextStartDate", null);
        } else {
            mav.addObject("nextStartDate", nextSemester.getStartDate());
        }

        mav.addObject("semester", semester);

        Exam exam = new Exam();
        exam.setSemester(semester);
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

        List<Assignment> allTeacherAssignmentsSemester = helper
                .getAssignmentsForSemesterList(helper.getAllTeacherStudentCourses(), day);
        mav.addObject("assignments", allTeacherAssignmentsSemester);

        return mav;
    }

}
