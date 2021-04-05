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

import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;

import ch.diedreifragezeichen.exama.assignments.assignment.Assignment;
import ch.diedreifragezeichen.exama.assignments.availablePrepTimes.AvailablePrepTime;
import ch.diedreifragezeichen.exama.assignments.availablePrepTimes.AvailablePrepTimeRepository;
import ch.diedreifragezeichen.exama.assignments.examTypes.ExamType;
import ch.diedreifragezeichen.exama.assignments.examTypes.ExamTypeRepository;
import ch.diedreifragezeichen.exama.assignments.exams.*;
import ch.diedreifragezeichen.exama.assignments.homeworks.Homework;
import ch.diedreifragezeichen.exama.assignments.workloadDistributions.WorkloadDistribution;
import ch.diedreifragezeichen.exama.assignments.workloadDistributions.WorkloadDistributionRepository;
import ch.diedreifragezeichen.exama.courses.CoreCourse;
import ch.diedreifragezeichen.exama.courses.CoreCourseRepository;
import ch.diedreifragezeichen.exama.courses.Course;
import ch.diedreifragezeichen.exama.courses.CourseRepository;
import ch.diedreifragezeichen.exama.semesters.*;
import ch.diedreifragezeichen.exama.subjects.*;
import ch.diedreifragezeichen.exama.users.Role;
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
    private UserRepository userRepo;

    @Autowired
    private RoleRepository roleRepo;

    @Autowired
    private CoreCourseRepository coreCourseRepo;

    @Autowired
    private CourseRepository courseRepo;

    @Autowired
    ExamTypeRepository examtypeRepo;

    @Autowired
    AvailablePrepTimeRepository availablePrepTimeRepo;

    @Autowired
    WorkloadDistributionRepository distributionRepo;

    @PersistenceContext
    private EntityManager em;

    /** USER RELATED SERVICES */

    /** Service 1a - Get current User */
    public User getCurrentUser() {
        Authentication authLoggedInUser = SecurityContextHolder.getContext().getAuthentication();
        return userRepo.findUserByEmail(authLoggedInUser.getName());
    }

    /** Service 1b - Get name of current User */
    public String getCurrentUserName() {
        Authentication authLoggedInUser = SecurityContextHolder.getContext().getAuthentication();
        return authLoggedInUser.getName();
    }

    /** Service 1c - Get roles of current User */
    public Set<Role> getCurrentUserRoles() {
        return getCurrentUser().getRoles();
    }

    /** Service 1d - Check role of current User by String */
    public boolean currentUserIsA(String name) {

        return getCurrentUserRoles().contains(roleRepo.findRoleByName(name));
    }

    /** Service 1d - Check role of current User by String */
    public boolean currentUserIsA(Long id) {

        return getCurrentUserRoles().contains(roleRepo.findRoleById(id));
    }

    /** ROLE RELATED SERVICES */

    /** Service 2a for student users */
    public List<Subject> getSubjectsOfAStudentUser() {
        User user = getCurrentUser();
        return user.getCourses().stream().filter(c -> Objects.nonNull(c.getSubject())).map(Course::getSubject)
                .sorted((c1, c2) -> c1.getId().compareTo(c2.getId())).collect(Collectors.toList());

    }

    /**
     * Sevice 2b for teacher users and above. Returns all the coreCourses of all the
     * students that a teacher has. This is used for the Teacher navBar
     */
    public List<CoreCourse> getAllTeacherStudentCoreCourses() {
        User user = getCurrentUser();
        return user.getCourses().stream().map(Course::getUsersList).flatMap(List::stream).distinct()
                .filter(u -> Objects.nonNull(u.getCoreCourse())).map(User::getCoreCourse).distinct()
                .sorted((c1, c2) -> c1.getId().compareTo(c2.getId())).collect(Collectors.toList());
    }

    /**
     * Service 2c for teacher users and above. Return all the courss of all the the
     * students that a teacher has. This is useful for exam setting
     */
    public List<Course> getAllTeacherStudentCourses() {
        User user = getCurrentUser();
        List<Course> listCourses = courseRepo.findAll();
        return listCourses.stream().filter(c -> c.getUsers().contains(user)).collect(Collectors.toList());

    }

    /**
     * Service 2d. Get all the exams that a teacher has
     */

    public List<Exam> getAllTeacherExams() throws NotFoundException {
        if (currentUserIsA("Student")) {
            throw new NotFoundException("The current User is not a teacher");
        }
        List<Course> allCourses = getAllTeacherStudentCourses();
        return allCourses.stream().filter(u -> Objects.nonNull(u.getExams())).map(Course::getExams)
                .flatMap(List::stream).distinct().filter(e -> e.getCreator().equals(getCurrentUser()))
                .sorted((e1, e2) -> e1.getDueDate().compareTo(e2.getDueDate())).collect(Collectors.toList());
    }

    /** Service 2d. Get all the exams that a teacher has */
    public List<Exam> getAllTeacherExamsFromCurrentDate() throws NotFoundException {
        return getAllTeacherExams().stream().filter(e -> e.getDueDate().isBefore(LocalDate.now()))
                .collect(Collectors.toList());
    }

    /**
     * Service 2d. Get all the homework that a teacher has assigned
     */

    public List<Homework> getAllTeacherHomework() throws NotFoundException {
        if (currentUserIsA("Student")) {
            throw new NotFoundException("The current User is not a teacher");
        }
        List<Course> allCourses = getAllTeacherStudentCourses();
        return allCourses.stream().filter(u -> Objects.nonNull(u.getHomeworks())).map(Course::getHomeworks)
                .flatMap(List::stream).distinct().filter(e -> e.getCreator().equals(getCurrentUser()))
                .sorted((e1, e2) -> e1.getDueDate().compareTo(e2.getDueDate())).collect(Collectors.toList());
    }

    /** Service 2d. Get all the exams that a teacher has */
    public List<Homework> getAllTeacherHomeworkFromCurrentDate() throws NotFoundException {
        return getAllTeacherHomework().stream().filter(e -> e.getDueDate().isBefore(LocalDate.now()))
                .collect(Collectors.toList());
    }

    /** DATE RELATED SERVICES */

    /** DateService 1 */
    public LocalDate getLocaldateFromString(String dateString) {
        return LocalDate.parse(dateString).with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
    }

    /** Service 11d - Return a list of all Dates between two dates */
    public List<LocalDate> getAllDatesBetweenAndWith(LocalDate start, LocalDate end) {
        long length = ChronoUnit.DAYS.between(start, end);
        return Stream.iterate(start, date -> date.plusDays(1)).limit(length + 1).collect(Collectors.toList());
    }

    /** Service 12 - returns the school semester of any given date */
    public Semester getCurrentSemesterBasedOnDate(LocalDate date) {// throws NotFoundException {
        List<Semester> earlierSemesters = semesterRepo.findAll().stream().filter(s -> s.isEnabled())
                .filter(s -> s.getStartDate().isBefore(date)).collect(Collectors.toList());

        return !earlierSemesters.isEmpty()
                ? earlierSemesters.stream().sorted((s1, s2) -> s1.compareTo(s2)).reduce((first, second) -> second).get()
                : null;

    }

    /** Service 11 */
    public List<LocalDate> getAllMondaysOfSemester(Semester semester) {
        /** retrieve semester Information and first / last day of Semester */
        LocalDate semesterStart = semester.getStartDate().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDate monday = semesterStart;
        LocalDate semesterEnd = semester.getEndDate().with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));

        /** create a list of all Mondays */
        List<LocalDate> allMondays = new ArrayList<>();
        while (monday.isBefore(semesterEnd)) {
            allMondays.add(monday);
            // Set up the loop.
            monday = monday.plusWeeks(1);
        }
        return allMondays;
    }

    /** ONGOING BLOCK */

    /** Service 6 - Calculate Workload Value */
    public double getOngoingWorkloadValueForSevenDays(Long coreCourseId, LocalDate monday) {

        Double[] workloadTotalDaysArray = getOngoingWorkloadTotalSevenDaysArray(coreCourseId, monday);

        // this method is not finished, but the value will define the background color
        double sum = 0;
        for (int i = 0; i < workloadTotalDaysArray.length; i++) {
            sum += workloadTotalDaysArray[i];
        }
        return sum;
    }

    /** Service 9a - Get ONGOING Workload total for each day of the week */
    public Double[] getOngoingWorkloadTotalSevenDaysArray(Long coreCourseId, LocalDate monday) {
        List<Course> allCourses = coreCourseRepo.findCoreCourseById(coreCourseId).getCourses();
        return getOngoingWorkloadTotalSevenDaysArray(allCourses, monday);
    }

    /** Service C - Get ONGOING Workload total for each day of the week */
    public Double[] getOngoingWorkloadTotalSevenDaysArray(List<Course> courses, LocalDate monday) {
        List<LocalDate> weekDays = getAllDatesBetweenAndWith(monday, monday.plusDays(6));
        // Calculate Workload Values for each day
        Double[] workloadTotalSevenDaysArray = new Double[7];
        {
            for (int i = 0; i < workloadTotalSevenDaysArray.length; i++) {
                LocalDate currentDay = weekDays.get(i);
                List<Assignment> assignments = getOngoingAssignmentsForDateList(courses, currentDay);
                workloadTotalSevenDaysArray[i] = Math.min(
                        assignments.stream().map(c -> c.getWorkloadValue(currentDay)).mapToDouble(w -> w).sum(), 1);
            }
        }
        return workloadTotalSevenDaysArray;
    }

    /**
     * Service B - return all the ONGOING Assignments for a week of a particular
     * user
     */
    public List<Assignment> getOngoingAssignmentsForDateList(List<Course> courses, LocalDate date) {

        List<Assignment> assignments = new ArrayList<>();
        assignments.addAll(getOngoingExamsForDateList(courses, date));
        assignments.addAll(getOngoingHomeworkForDateList(courses, date));

        return assignments;
    }
    /**
     * Service Aa - Get every ONGOING exam for a particular DATE for List of
     * courses
     */
    public List<Exam> getOngoingExamsForDateList(List<Course> courses, LocalDate date) {
        // add every exam for the week
        return courses.stream().filter(c -> Objects.nonNull(c.getExams())).map(c -> c.getExams()).flatMap(List::stream)
                .distinct().filter(e -> e.getDueDate().isAfter(date.minusDays(1)))
                .filter(e -> Objects.nonNull(e.getRealStartDate())).filter(e -> e.getRealStartDate().isBefore(date.plusDays(1)))
                .collect(Collectors.toList());
    }

    /**
     * Service Ab - Get every ONGOING exam for a particular DATE for List of
     * courses
     */
    public List<Homework> getOngoingHomeworkForDateList(List<Course> courses, LocalDate date) {
        // add every exam for the week
        return courses.stream().filter(c -> Objects.nonNull(c.getHomeworks())).map(c -> c.getHomeworks())
                .flatMap(List::stream).distinct().filter(e -> e.getDueDate().isAfter(date.minusDays(1)))
                .filter(h -> Objects.nonNull(h.getRealStartDate())).filter(h -> h.getRealStartDate().isBefore(date.plusDays(1)))
                .collect(Collectors.toList());
    }


    /** ONGOING BLOCK END */


    /** WORKLOAD RELATED SERVICES */

    /**
     * Service 5 - Returns a list of weekly Workload values for every week of the
     * semester
     */
    public List<Double> getSemesterWorkloadList(Long coreCourseId, Long semesterId) {
        Semester semester = semesterRepo.findSemesterById(semesterId);
        List<LocalDate> allMondays = getAllMondaysOfSemester(semester);

        Iterator<LocalDate> allMondaysIterator = allMondays.iterator();
        // For every single Monday of the semester, call getWorkloadTotalWeek and add to
        // list.
        List<Double> workloadForEveryWeekList = new ArrayList<>();
        while (allMondaysIterator.hasNext()) {
            workloadForEveryWeekList.add(getOngoingWorkloadValueForSevenDays(coreCourseId, allMondaysIterator.next()));
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

    public ModelAndView getAssignmentBoxInformation(ModelAndView mav, Semester currentSemester) {
        Exam exam = new Exam();
        exam.setSemester(currentSemester);
        mav.addObject("exam", exam);

        Homework homework = new Homework();
        mav.addObject("homework", homework);

        // Assignment Creation Box Elements
        List<Course> teacherStudentCourses = getAllTeacherStudentCourses();
        mav.addObject("allCourses", teacherStudentCourses);
        List<ExamType> listTypes = examtypeRepo.findAll();
        mav.addObject("allExamTypes", listTypes);
        List<AvailablePrepTime> listPrepTimes = availablePrepTimeRepo.findAll();
        mav.addObject("allPrepTimes", listPrepTimes);
        List<WorkloadDistribution> listDist = distributionRepo.findAll();
        mav.addObject("allWorkloadDistributions", listDist);
        return mav;
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

    /** Service 2a */
    public double calculateExamFactor(List<Exam> listExams) {
        return listExams.stream().filter(e -> Objects.nonNull(e.getCountingFactor()))
                .mapToDouble(exam -> exam.getCountingFactor()).sum();
    }

    /** Service 2b */
    public Model calculateExamFactor(Model model, List<Exam> listExams) {
        double ExamFactor = calculateExamFactor(listExams);
        model.addAttribute("xFactor", ExamFactor);
        // Schreibe im html: <h1 data-th-text="${anzpr}" />
        return model;
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

    /**
     * Service 9aa - return all the Assignments for a semester of a number of
     * courses
     */
    public List<Assignment> getAssignmentsForSemesterList(List<Course> courses, LocalDate monday) {
        List<Assignment> assignments = new ArrayList<>();
        // add every exam for the week
        assignments.addAll(getExamsForSemesterList(courses, monday));

        // add every homework for the week
        assignments.addAll(getHomeworkForSemesterList(courses, monday));

        return assignments.stream().sorted((e1, e2) -> e1.getDueDate().compareTo(e2.getDueDate()))
                .collect(Collectors.toList());
    }

    /** Service 9b - return all the Assignments for a week of a particular course */
    public List<Assignment> getAssignmentsForSevenDaysList(Course course, LocalDate monday) {
        List<Course> courses = new ArrayList<>();
        courses.add(course);
        return getAssignmentsForSevenDaysList(courses, monday);
    }

    /**
     * Service 9bb - return all the Assignments for a week of a particular course
     */
    public List<Assignment> getAssignmentsForSemesterList(Course course, LocalDate monday) {
        List<Course> courses = new ArrayList<>();
        courses.add(course);
        return getAssignmentsForSemesterList(courses, monday);
    }

    /** Service 9c - return all the Assignments for a week of a particular user */
    public List<Assignment> getAssignmentsForSevenDaysList(User user, LocalDate monday) {

        List<Course> courses = new ArrayList<>(user.getCourses());
        return getAssignmentsForSevenDaysList(courses, monday);
    }

    /** Service 9cc - return all the Assignments for a week of a particular user */
    public List<Assignment> getAssignmentsForSemesterList(User user, LocalDate monday) {

        List<Course> courses = new ArrayList<>(user.getCourses());
        return getAssignmentsForSemesterList(courses, monday);
    }

    /** Service 10a - Get every exam for a particular week for List of courses */
    public List<Exam> getExamsForSevenDaysList(List<Course> courses, LocalDate monday) {
        List<Exam> exams = new ArrayList<>();
        // add every exam for the week
        courses.stream().filter(c -> Objects.nonNull(c.getExams())).map(c -> c.getExams()).flatMap(List::stream)
                .distinct().filter(e -> e.getDueDate().isAfter(monday.minusDays(1)))
                .filter(e -> e.getDueDate().isBefore(monday.plusDays(7)))
                .sorted((e1, e2) -> e1.getDueDate().compareTo(e2.getDueDate()))
                .collect(Collectors.toCollection(() -> exams));

        return exams;
    }

    /**
     * Service 10aa - Get every exam for a particular Semester for List of courses
     */
    public List<Exam> getExamsForSemesterList(List<Course> courses, LocalDate monday) {
        List<Exam> exams = new ArrayList<>();
        // add every exam for the week
        courses.stream().filter(c -> Objects.nonNull(c.getExams())).map(c -> c.getExams()).flatMap(List::stream)
                .distinct()
                .filter(e -> e.getDueDate().isAfter(getCurrentSemesterBasedOnDate(monday).getStartDate().minusDays(1)))
                .filter(e -> e.getDueDate().isBefore(getCurrentSemesterBasedOnDate(monday).getEndDate().plusDays(1)))
                .collect(Collectors.toCollection(() -> exams));
        return exams;
    }

    public HashMap<String, Exam> getWeeklySemesterExamMap(Semester semester) {

        return null;
    }

    /** Service 10b - Get every exam for a particular week for single course */
    public List<Exam> getExamsForSevenDaysList(Course course, LocalDate monday) {
        List<Course> courses = new ArrayList<>();
        courses.add(course);
        return getExamsForSevenDaysList(courses, monday);
    }

    /**
     * Service 10c - Get every homework for a particular week for list of courses
     */
    public List<Homework> getHomeworkForSevenDaysList(List<Course> courses, LocalDate monday) {
        List<Homework> homework = new ArrayList<>();
        // add every homework for the week
        courses.stream().filter(c -> Objects.nonNull(c.getHomeworks())).map(c -> c.getHomeworks()).flatMap(List::stream)
                .distinct().filter(e -> e.getDueDate().isAfter(monday.minusDays(1)))
                .filter(e -> e.getDueDate().isBefore(monday.plusDays(7)))
                .sorted((e1, e2) -> e1.getDueDate().compareTo(e2.getDueDate()))
                .collect(Collectors.toCollection(() -> homework));

        return homework;
    }

    /**
     * Service 10cc - Get every homework for a particular Semester for list of
     * courses
     */
    public List<Homework> getHomeworkForSemesterList(List<Course> courses, LocalDate monday) {
        List<Homework> homework = new ArrayList<>();
        // add every homework for the week
        courses.stream().filter(c -> Objects.nonNull(c.getHomeworks())).map(c -> c.getHomeworks()).flatMap(List::stream)
                .distinct()
                .filter(e -> e.getDueDate().isAfter(getCurrentSemesterBasedOnDate(monday).getStartDate().minusDays(1)))
                .filter(e -> e.getDueDate().isBefore(getCurrentSemesterBasedOnDate(monday).getEndDate().plusDays(1)))
                .sorted((e1, e2) -> e1.getDueDate().compareTo(e2.getDueDate()))
                .collect(Collectors.toCollection(() -> homework));

        return homework;
    }

    /** Service 10d - Get every homework for a particular week for single course */
    public List<Homework> getHomeworkForSevenDaysList(Course course, LocalDate monday) {
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

    /** ORGANISATIONAL SERVICES */

    /** Service 13a */
    public List<Course> getAllCoursesOfACoreCourse(CoreCourse coreCourse) {

        return coreCourse.getStudents().stream().filter(u -> Objects.nonNull(u.getCourses())).map(User::getCoursesList)
                .flatMap(List::stream).distinct().collect(Collectors.toList());

    }

    /** Service 13b */
    public List<Subject> getAllSubjectsOfACoreCourse(CoreCourse coreCourse) {
        List<Course> coursesOfCourseCourse = getAllCoursesOfACoreCourse(coreCourse);
        return coursesOfCourseCourse.stream().map(Course::getSubject).distinct()
                .sorted((c1, c2) -> c1.getId().compareTo(c2.getId())).collect(Collectors.toList());

    }

    /** Service 3a */
    public long calculateNumberOfSubjects(List<Subject> allSubjects) {
        return allSubjects.stream().filter(s -> Objects.nonNull(s.getId())).map(s -> s.getId()).distinct().count();
    }

    /** Service 3b */
    public ModelAndView calculateNumberOfSubjects(ModelAndView mav, List<Subject> allSubjects) {
        long numberOfSubjects = calculateNumberOfSubjects(allSubjects);
        mav.addObject("numberOfSubjects", numberOfSubjects);
        return mav;
    }

}