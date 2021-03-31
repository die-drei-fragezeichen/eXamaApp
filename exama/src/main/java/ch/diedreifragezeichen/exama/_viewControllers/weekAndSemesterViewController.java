package ch.diedreifragezeichen.exama._viewControllers;

import java.time.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import ch.diedreifragezeichen.exama._services.AppService;
import ch.diedreifragezeichen.exama.assignments.assignment.Assignment;
import ch.diedreifragezeichen.exama.assignments.availablePrepTimes.AvailablePrepTime;
import ch.diedreifragezeichen.exama.assignments.availablePrepTimes.AvailablePrepTimeRepository;
import ch.diedreifragezeichen.exama.assignments.examTypes.ExamType;
import ch.diedreifragezeichen.exama.assignments.examTypes.ExamTypeRepository;
import ch.diedreifragezeichen.exama.assignments.exams.Exam;
import ch.diedreifragezeichen.exama.assignments.homeworks.Homework;
import ch.diedreifragezeichen.exama.assignments.workloadDistributions.WorkloadDistribution;
import ch.diedreifragezeichen.exama.assignments.workloadDistributions.WorkloadDistributionRepository;
import ch.diedreifragezeichen.exama.courses.*;
import ch.diedreifragezeichen.exama.semesters.Holiday;
import ch.diedreifragezeichen.exama.semesters.HolidayRepository;
import ch.diedreifragezeichen.exama.semesters.Semester;
import ch.diedreifragezeichen.exama.users.User;
import javassist.NotFoundException;

@Controller
public class weekAndSemesterViewController {
        @Autowired
        CoreCourseRepository coreCourseRepo;
        @Autowired
        HolidayRepository holidayRepo;
        @Autowired
        ExamTypeRepository examtypeRepo;
        @Autowired
        AvailablePrepTimeRepository availablePrepTimeRepo;
        @Autowired
        WorkloadDistributionRepository distributionRepo;
        @Autowired
        AppService helper;
        

        @GetMapping("/calendarchoose")
        public String calendarChoose(@RequestParam(name = "view") Long viewId,
                        @RequestParam(name = "coreCourse") Long coreCourseId) {

                if (viewId == 1) {// if viewId = 1: redirect User to Workloaddiagram

                        return "redirect:/calendar?view=" + viewId + "&monday=" + LocalDate.now().with(DayOfWeek.MONDAY)
                                        + "&coreCourse=" + coreCourseId;

                }
                if (viewId == 3) { // if viewID = 3: redirect User to SemesterView
                        return "redirect:/calendar?view=" + viewId + "&coreCourse=" + coreCourseId;

                }
                return "redirect:/";
        }

        @GetMapping("/calendar")
        public ModelAndView workloadDiagram(@RequestParam(name = "view") Long viewId,
                        @RequestParam(name = "monday") String mondayString,
                        @RequestParam(name = "coreCourse") Long coreCourseId) throws NotFoundException {
                /** Security Check */
                User user = helper.getCurrentUser();
                List<CoreCourse> userCoreCourses = user.getCoreCourses();
                CoreCourse selectedCourse = coreCourseRepo.findCoreCourseById(coreCourseId);
                // if chosen CoreCourse empty, if coreCourse wrong -> redirect home
                // TODO: later errorpage
                if (selectedCourse == null
                                || (helper.currentUserIsA("Student") && !user.getCoreCourse().equals(selectedCourse))
                                || (helper.currentUserIsA("Teacher") && !userCoreCourses.contains(selectedCourse))) {
                        return new ModelAndView("redirect:/");
                }

                // manage user directing
                ModelAndView mav;
                if (viewId == 1) {// direct to workloadDiagram
                        mav = new ModelAndView("teacherTemplates/workloadDiagram.html");
                }
                else if (viewId == 2) {
                        // direct to weeklyView
                        mav = new ModelAndView("teacherTemplates/weeklyView.html");
                } else { // viewId == 3
                        mav = new ModelAndView("teacherTemplates/semesterViewShow.html");
                }

                LocalDate monday = helper.getLocaldateFromString(mondayString);
                mav.addObject("coreCourse", selectedCourse);
                // add all necessary dates for scrolling
                mav.addObject("monday", monday);
                mav.addObject("nextMonday", monday.plusWeeks(1));
                mav.addObject("lastMonday", monday.minusWeeks(1));

                List<Course> selectedCourses = selectedCourse.getCourses();

                if (viewId == 1) {
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
                if (viewId == 2) {
                        // add all ASSIGNMENTS for ONE weekView
                        List<Assignment> assignments = helper.getAssignmentsForSevenDaysList(selectedCourses, monday);
                        mav.addObject("assignments", assignments);
                        // add ALL EXAMS for examBar
                        List<Exam> exams = helper.getExamsForSevenDaysList(selectedCourses, monday);
                        mav.addObject("allWeekExams", exams);

                } else { // viewId == 3
                         // TODO: figure out how to steer this
                        Semester currentSemester = helper.getCurrentSemesterBasedOnDate(LocalDate.now());
                        Semester chosenSemester = currentSemester;
                        mav.addObject("semester", chosenSemester);

                        List<LocalDate> allMondays = helper.getAllMondaysOfSemester(chosenSemester);
                        mav.addObject("allMondays", allMondays);

                        mav.addObject("userSubjects", helper
                                        .getAllSubjectsOfACoreCourse(coreCourseRepo.findCoreCourseById(coreCourseId)));

                        List<Course> userCourses = new ArrayList<Course>(user.getCourses());
                        LocalDate semesterStart = chosenSemester.getStartDate();
                        LocalDate semesterEnd = chosenSemester.getEndDate();
                        monday = semesterStart;
                        List<HashMap<String, Exam>> allExams = new ArrayList<>();
                        CoreCourse coreCourse = coreCourseRepo.findCoreCourseById(coreCourseId);
                        List<Course> coreCourseCourses = helper.getAllCoursesOfACoreCourse(coreCourse);
                        while (monday.isBefore(semesterEnd.plusDays(1))) {
                                List<Exam> examsByWeek = helper.getExamsForSevenDaysList(coreCourseCourses, monday);
                                HashMap<String, Exam> map = new HashMap<>();
                                for (Exam exam : examsByWeek) {
                                        map.put(exam.getCourse().getSubject().getTag(), exam);
                                }
                                allExams.add(map);
                                monday = monday.plusWeeks(1);
                        }

                        mav.addObject("allExams", allExams);

                        /** create Hashmap mapping every single day of the holiday with holiday */
                        monday = semesterStart;
                        List<Holiday> listHolidays = holidayRepo.findAllByStartDateBetween(semesterStart, semesterEnd);
                        HashMap<LocalDate, Holiday> allHolidayDates = new HashMap<>();
                        for (Holiday holiday : listHolidays) {
                                List<LocalDate> allHolyDates = helper.getAllDatesBetweenAndWith(holiday.getStartDate(),
                                                holiday.getEndDate());
                                for (LocalDate date : allHolyDates) {
                                        allHolidayDates.put(date, holiday);
                                }
                        }

                        mav.addObject("allHolidayDates", allHolidayDates);

                        // get a list of all the weekly workloads for every week
                        List<Double> weeklyWorkload = helper.getSemesterWorkloadList(coreCourseId, currentSemester.getId());
                        mav.addObject("weeklyWorkload", weeklyWorkload);

                        Exam exam = new Exam();
                        mav.addObject("exam", exam);

                        Homework homework = new Homework();
                        mav.addObject("homework", homework);

                        exam.setSemester(currentSemester);

                        List<Course> teacherStudentCourses = helper.getAllTeacherStudentCourses();
                        mav.addObject("allCourses", teacherStudentCourses);

                        List<ExamType> listTypes = examtypeRepo.findAll();
                        mav.addObject("allExamTypes", listTypes);

                        List<AvailablePrepTime> listPrepTimes = availablePrepTimeRepo.findAll();
                        mav.addObject("allPrepTimes", listPrepTimes);

                        List<WorkloadDistribution> listDist = distributionRepo.findAll();
                        mav.addObject("allWorkloadDistributions", listDist);

                }

                return mav;
        }
}
