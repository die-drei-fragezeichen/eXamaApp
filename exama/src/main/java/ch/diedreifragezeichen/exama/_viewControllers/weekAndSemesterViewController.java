package ch.diedreifragezeichen.exama._viewControllers;

import java.time.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ch.diedreifragezeichen.exama._services.AppService;
import ch.diedreifragezeichen.exama.assignments.assignment.Assignment;
import ch.diedreifragezeichen.exama.assignments.availablePrepTimes.AvailablePrepTimeRepository;
import ch.diedreifragezeichen.exama.assignments.examTypes.ExamTypeRepository;
import ch.diedreifragezeichen.exama.assignments.exams.Exam;
import ch.diedreifragezeichen.exama.assignments.exams.ExamRepository;
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
        ExamRepository examRepo;
        @Autowired
        AppService helper;

        @GetMapping("/calendar")
        public ModelAndView workloadDiagram(@RequestParam(name = "view") Long viewId,
                        @RequestParam(name = "day", required = false) String dayString,
                        @RequestParam(name = "coreCourse") Long coreCourseId) throws NotFoundException {
                /** Security Check */
                User user = helper.getCurrentUser();
                List<CoreCourse> userCoreCourses = helper.currentUserIsA("Teacher")
                                ? helper.getAllTeacherStudentCoreCourses()
                                : null;
                CoreCourse selectedCourse = coreCourseRepo.findCoreCourseById(coreCourseId);
                // if chosen CoreCourse empty, if coreCourse wrong -> redirect home
                if (selectedCourse == null
                                || (helper.currentUserIsA("Student") && !user.getCoreCourse().equals(selectedCourse))
                                || (helper.currentUserIsA("Teacher") && !userCoreCourses.contains(selectedCourse))) {
                        return new ModelAndView("redirect:/");
                }

                // manage user directing
                ModelAndView mav;
                if (viewId == 1) {
                        // direct to diagramView
                        mav = new ModelAndView("generalTemplates/weekDiagramView.html");
                } else if (viewId == 2) {
                        // direct to listView
                        mav = new ModelAndView("generalTemplates/weekListView.html");
                } else if (viewId == 3) {
                        // direct to semesterView
                        mav = new ModelAndView("generalTemplates/semesterListView.html");
                } else {
                        // direct to diagramView
                        mav = new ModelAndView("generalTemplates/weekDiagramView.html");
                }
                LocalDate day = LocalDate.now();
                if (dayString != null) {
                day = helper.getLocaldateFromString(dayString);
                }
                LocalDate monday = day.with(DayOfWeek.MONDAY);
                
               
                mav.addObject("coreCourse", selectedCourse);
                // add all necessary dates for scrolling
                // TODO: necessary for all views?
                mav.addObject("monday", monday);
                mav.addObject("nextMonday", monday.plusWeeks(1));
                mav.addObject("lastMonday", monday.minusWeeks(1));

                // add all necessary objects for assignment creation into mav
                Semester chosenSemester = helper.getCurrentSemesterBasedOnDate(day.plusDays(1));
                helper.getAssignmentBoxInformation(mav, chosenSemester);

                List<Course> selectedCourses = selectedCourse.getCourses();
                if (viewId == 1) {
                        // For WORKLOAD DIAGRAM add all workload, the eXam Factor and Exams for two
                        // weeks (all of which will be displayed)
                        Double[] workloadTotalDaysList = helper.getOngoingWorkloadTotalSevenDaysArray(coreCourseId, monday);
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
                        mav.addObject("semester", chosenSemester);
                        // add semster navigation information
                        Semester prevSemester = helper
                                        .getCurrentSemesterBasedOnDate(chosenSemester.getStartDate().minusDays(182));
                        if (prevSemester == null) {
                                mav.addObject("prevStartDate", null);
                        } else {
                                mav.addObject("prevStartDate", prevSemester.getStartDate().plusDays(1));
                        }
                        Semester nextSemester = helper
                                        .getCurrentSemesterBasedOnDate(chosenSemester.getEndDate().plusDays(182));

                        if (nextSemester == null) {
                                mav.addObject("nextStartDate", null);
                        } else {
                                mav.addObject("nextStartDate", nextSemester.getStartDate().plusDays(1));
                        }

                        List<LocalDate> allMondays = helper.getAllMondaysOfSemester(chosenSemester);
                        mav.addObject("allMondays", allMondays);
                        // used to define current week in semester view
                        List<LocalDate> allDaysForTheWeek = helper.getAllDatesBetweenAndWith(
                                        LocalDate.now().with(DayOfWeek.MONDAY), LocalDate.now().with(DayOfWeek.SUNDAY));
                        mav.addObject("allDaysForTheWeek", allDaysForTheWeek);

                        mav.addObject("userSubjects", helper
                                        .getAllSubjectsOfACoreCourse(coreCourseRepo.findCoreCourseById(coreCourseId)));

                        // create Exam Hashmap for semester view, starting with the first Monday of the
                        // semester
                        monday = chosenSemester.getStartDate().with(DayOfWeek.MONDAY);
                        List<HashMap<String, Exam>> allExams = new ArrayList<>();
                        CoreCourse coreCourse = coreCourseRepo.findCoreCourseById(coreCourseId);
                        List<Course> coreCourseCourses = helper.getAllCoursesOfACoreCourse(coreCourse);
                        while (monday.isBefore(chosenSemester.getEndDate().plusDays(1))) {
                                List<Exam> examsByWeek = helper.getExamsForSevenDaysList(coreCourseCourses, monday);
                                HashMap<String, Exam> map = new HashMap<>();
                                for (Exam e : examsByWeek) {
                                        map.put(e.getCourse().getSubject().getTag(), e);
                                }
                                allExams.add(map);
                                monday = monday.plusWeeks(1);
                        }

                        mav.addObject("allExams", allExams);

                        // create Hashmap mapping every single day of the holiday with holiday
                        monday = chosenSemester.getStartDate().with(DayOfWeek.MONDAY);
                        List<Holiday> listHolidays = holidayRepo.findAllByStartDateBetween(
                                        chosenSemester.getStartDate(), chosenSemester.getEndDate());
                        HashMap<LocalDate, Holiday> allHolidayDates = new HashMap<>();
                        for (Holiday holiday : listHolidays) {
                                List<LocalDate> allHolyDates = helper.getAllDatesBetweenAndWith(holiday.getStartDate(),
                                                holiday.getEndDate());
                                for (LocalDate date : allHolyDates) {
                                        allHolidayDates.put(date, holiday);
                                }
                        }

                        mav.addObject("allHolidayDates", allHolidayDates);

                        // get a list of all the weekly workloads for every week (used for background
                        // colour)
                        List<Double> weeklyWorkload = helper.getSemesterWorkloadList(coreCourseId,
                                        chosenSemester.getId());
                        mav.addObject("weeklyWorkload", weeklyWorkload);

                }

                return mav;
        }

        @GetMapping("/exams/detail/{id}")
        public String viewOrEditExam(@PathVariable("id") long id, Model model, RedirectAttributes ra) {
                Exam exam = examRepo.findExamById(id);
                model.addAttribute("exam", exam);
                model.addAttribute("pageTitle", "Prüfung: " + exam.getName());
                return "generalTemplates/examDetails";
        }
}
