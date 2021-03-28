package ch.diedreifragezeichen.exama._services;

import org.springframework.stereotype.Service;
import java.time.*;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.stream.*;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.*;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import ch.diedreifragezeichen.exama.assignments.assignment.Assignment;
import ch.diedreifragezeichen.exama.assignments.exams.*;
import ch.diedreifragezeichen.exama.courses.CoreCourse;
import ch.diedreifragezeichen.exama.courses.CoreCourseRepository;
import ch.diedreifragezeichen.exama.courses.Course;
import ch.diedreifragezeichen.exama.operator.*;
import ch.diedreifragezeichen.exama.semesters.*;
import ch.diedreifragezeichen.exama.subjects.*;
import ch.diedreifragezeichen.exama.users.RoleRepository;
import ch.diedreifragezeichen.exama.users.User;
import ch.diedreifragezeichen.exama.users.UserRepository;
import javassist.NotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Service("AppService")
public class AppService {

    @Autowired
    private SemesterRepository semesterRepo;

    @Autowired
    private HolidayRepository holidayRepo;

    @Autowired
    private ExamRepository examRepo;

    @Autowired
    private SubjectRepository subjectRepo;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private RoleRepository roleRepo;

    @Autowired
    private CoreCourseRepository coreCourseRepo;

    @PersistenceContext
    private EntityManager em;

    // * WORKLOAD RELATED SERVICES */

    /**
     * Service 5 - Returns a list of weekly Workload values for every Monday of the
     * semester
     */
    public List<Double> getSemesterWorkloadList(Long coreCourseId, LocalDate monday, List<LocalDate> allMondays) {
        Iterator<LocalDate> allMondaysIterator = allMondays.iterator();
        // For every single Monday of the semester, call getWorkloadTotalWeek and add to
        // list.
        List<Double> workloadForEveryWeekList = new ArrayList<>();
        while (allMondaysIterator.hasNext()) {
            workloadForEveryWeekList.add(getWorkloadValueForSevenDays(coreCourseId, allMondaysIterator.next()));
        }
        return workloadForEveryWeekList;

    }

    /** Service 6 - Calculate Workload Value */
    public double getWorkloadValueForSevenDays(Long coreCourseId, LocalDate monday) {

        Double[] workloadTotalDaysArray = getWorkloadValueForSevenDaysArray(coreCourseId, monday);

        // this method is not finished, but the value will define the background color
        double sum = 0;
        for (int i = 0; i < workloadTotalDaysArray.length; i++) {
            sum += workloadTotalDaysArray[i];
        }
        return sum;
    }

    /** Service 6a - get a Workload Array for the week */
    public Double[] getWorkloadValueForSevenDaysArray(Long coreCourseId, LocalDate monday) {
        List<Course> allCourses = coreCourseRepo.findCoreCourseById(coreCourseId).getCourses();

        List<Assignment> assignments = getAssignmentsForSevenDaysList(allCourses, monday);

        return getWorkloadValueForEachDayofWeek(assignments, monday);

    }

    /** Service 6b - get a Workload Array for the week */
    public Double[] getWorkloadValueForSevenDaysArray(Course course, LocalDate monday) {

        List<Course> courses = new ArrayList<>();
        courses.add(course);
        List<Assignment> assignments = getAssignmentsForSevenDaysList(courses, monday);

        return getWorkloadValueForEachDayofWeek(assignments, monday);
    }

    /** Service 6c - get a Workload Array for the week */
    public Double[] getWorkloadValueForSevenDaysArray(User user, LocalDate monday) {

        List<Course> allCourses = new ArrayList<>(user.getCourses());
        List<Assignment> assignments = getAssignmentsForSevenDaysList(allCourses, monday);
        return getWorkloadValueForEachDayofWeek(assignments, monday);

    }

    /** Service 7 - Calculate Workload Values for each day */
    public Double[] getWorkloadValueForEachDayofWeek(List<Assignment> assignments, LocalDate monday) {

        // Calculate Workload Values for each day
        Double[] workloadTotalSevenDaysArray = new Double[7];
        if (assignments != null) {
            for (int i = 0; i < workloadTotalSevenDaysArray.length; i++) {
                int j = i;
                workloadTotalSevenDaysArray[i] = Math.min(
                        assignments.stream().map(c -> c.getWorkloadValue(monday.plusDays(j))).mapToDouble(w -> w).sum(),
                        1);
            }
        }
        return workloadTotalSevenDaysArray;
    }

    // * ASSIGNMENT RELATED SERVICES */

    /** Service 1a */

    public long calculateNumberOfExam(List<Exam> allExams) {

        return allExams.stream().filter(e -> Objects.nonNull(e.getId())).map(e -> e.getId()).distinct().count();
    }

    /** Service 1b */
    public long calculateNumberOfExams(List<List<Exam>> allExams) {

        return allExams.stream().flatMap(List::stream).filter(e -> Objects.nonNull(e.getId())).map(e -> e.getId())
                .distinct().count();
    }

    public Model calculateNumberOfExams(Model model, List<Exam> listExams) {
        long anzahlExamen = calculateNumberOfExam(listExams);
        model.addAttribute("anzpr", anzahlExamen);
        // Schreibe im html zum Beispiel: <h1 data-th-text="${anzpr}" />
        return model;
    }

    /** Hilfsmethode 2a */
    public double calculateExamFactor(List<Exam> listExams) {
        return listExams.stream().filter(e -> Objects.nonNull(e.getCountingFactor()))
                .mapToDouble(exam -> exam.getCountingFactor()).sum();
    }

    /** Hilfsmethode 2b */
    public Model calculateExamFactor(Model model, List<Exam> listExams) {
        double ExamFactor = calculateExamFactor(listExams);
        model.addAttribute("xFactor", ExamFactor);
        // Schreibe im html: <h1 data-th-text="${anzpr}" />
        return model;
    }

    /** Hilfsmethode 3a */
    public long calculateNumberOfSubjects(List<Subject> allSubjects) {
        return allSubjects.stream().filter(s -> Objects.nonNull(s.getId())).map(s -> s.getId()).distinct().count();
    }

    /** Hilfsmethode 3b */
    public ModelAndView calculateNumberOfSubjects(ModelAndView mav, List<Subject> allSubjects) {
        long numberOfSubjects = calculateNumberOfSubjects(allSubjects);
        mav.addObject("numberOfSubjects", numberOfSubjects);
        return mav;
    }

    /** Service 9a - return all the Assignments for a week of a number of courses */
    public List<Assignment> getAssignmentsForSevenDaysList(List<Course> courses, LocalDate monday) {
        List<Assignment> assignments = new ArrayList<>();
        // add every exam for the week
        assignments.addAll(getExamsForSevenDaysList(courses, monday));

        // add every homework for the week
        assignments.addAll(getHomeworkForSevenDaysList(courses, monday));

        return assignments.stream().sorted((e1, e2) -> e1.getDueDate().compareTo(e2.getDueDate()))
                .collect(Collectors.toList());
    }

    /** Service 9b - return all the Assignments for a week of a particular course */
    public List<Assignment> getAssignmentsForSevenDaysList(Course course, LocalDate monday) {
        List<Course> courses = new ArrayList<>();
        courses.add(course);
        return getAssignmentsForSevenDaysList(courses, monday);
    }

    /** Service 9c - return all the Assignments for a week of a particular user */
    public List<Assignment> getAssignmentsForSevenDaysList(User user, LocalDate monday) {

        List<Course> courses = new ArrayList<>(user.getCourses());
        return getAssignmentsForSevenDaysList(courses, monday);
    }

    /** Service 10a - Get every exam for a particular week for List of courses*/
    public List<Assignment> getExamsForSevenDaysList(List<Course> courses, LocalDate monday) {
        List<Assignment> exams = new ArrayList<>();
        // add every exam for the week
        courses.stream().filter(c -> Objects.nonNull(c.getExams())).map(c -> c.getExams()).flatMap(List::stream)
                .distinct().filter(e -> e.getDueDate().isAfter(monday.minusDays(1)))
                .filter(e -> e.getDueDate().isBefore(monday.plusDays(7))).collect(Collectors.toCollection(() -> exams));

        return exams;
    }

    /** Service 10b - Get every exam for a particular week for single course*/
    public List<Assignment> getExamsForSevenDaysList(Course course, LocalDate monday) {
        List<Course> courses = new ArrayList<>();
        courses.add(course);
        return getExamsForSevenDaysList(courses, monday);
    }

    /** Service 10c - Get every homework for a particular week for list of courses */
    public List<Assignment> getHomeworkForSevenDaysList(List<Course> coreCourses, LocalDate monday) {
        List<Assignment> homework = new ArrayList<>();
        // add every homework for the week
        coreCourses.stream().filter(c -> Objects.nonNull(c.getHomeworks())).map(c -> c.getHomeworks())
                .flatMap(List::stream).distinct().filter(e -> e.getDueDate().isAfter(monday.minusDays(1)))
                .sorted((e1, e2) -> e1.getDueDate().compareTo(e2.getDueDate()))
                .collect(Collectors.toCollection(() -> homework));

        return homework;
    }

    /** Service 10d - Get every homework for a particular week for single course*/
    public List<Assignment> getHomeworkForSevenDaysList(Course course, LocalDate monday) {
        List<Course> courses = new ArrayList<>();
        courses.add(course);
        return getHomeworkForSevenDaysList(courses, monday);
    }

    // * HOLIDAY RELATED SERVICES */

    /** Service 11a - Get List of Holidays */
    public List<Holiday> getSemesterHolidayList(Semester semester) {
        // return a list of holidays between monday to sunday
        return holidayRepo.findAllByStartDateBetween(semester.getStartDate(), semester.getStartDate());
    }

    /** Service 11b - Get List of Holidays that start during this week */
    public List<Holiday> getHolidayStartDuringWeekList(LocalDate date) {
        LocalDate monday = date.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDate sunday = date.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));
        return holidayRepo.findAllByStartDateBetween(monday, sunday);
    }

    /** Service 11c - Get List of Holidays that start during this week */
    public HashMap<LocalDate, Holiday> getHolidayStartDuringWeekMap(LocalDate date) {

        List<Holiday> allHolidaysStartingThisWeek = getHolidayStartDuringWeekList(date);
        HashMap<LocalDate, Holiday> allHolidaysMap = new HashMap<>();

        for (Holiday holiday : allHolidaysStartingThisWeek) {
            allHolidaysMap.put(holiday.getStartDate(), holiday);

        }
        return allHolidaysMap;

    }
}
