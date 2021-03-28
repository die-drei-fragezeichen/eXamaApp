package ch.diedreifragezeichen.exama._viewControllers;

import java.time.*;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.stream.*;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.*;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import ch.diedreifragezeichen.exama._services.AppService;
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

@Controller
public class semesterViewController {
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

    @Autowired
    private AppService helper;

    /**
     * The following methods handle the Semester view creation
     * 
     * @throws NotFoundException
     */

    @GetMapping("/semesterView")
    public ModelAndView initializeView() throws NotFoundException {

        Semester currentSemester = helper.getCurrentSemesterBasedOnDate(LocalDate.now());

        return selectSemesterView(currentSemester.getId());
    }

    // mapping following semester selection
    @GetMapping("/semesterView/select")
    public ModelAndView selectSemesterView(@RequestParam(name = "selectedSemester") Long semesterId)
            throws NotFoundException {
        // get current User
        User user = helper.getCurrentUser();
        // if user is student get coreCourse id and redirect accordingly
        if (helper.currentUserIsA("Student")) {
            CoreCourse userCoreCourse = user.getCoreCourse();
            if (userCoreCourse == null) {
                throw new NotFoundException("CoreCourse not assigned");
            }
            return showSemesterView(semesterId, userCoreCourse.getId());

        } else {
            /**
             * Else user is not a student. User will see the actual coreCourse subject list.
             * So user is redirected with its first coreCourse
             */
            CoreCourse firstCoreCourse = user.getCourses().stream().map(Course::getUsersList).flatMap(List::stream)
                    .distinct().filter(u -> Objects.nonNull(u.getCoreCourse())).map(User::getCoreCourse).distinct()
                    .sorted((c1, c2) -> c1.getId().compareTo(c2.getId())).findFirst().orElse(null);
            if (firstCoreCourse == null) {
                throw new NotFoundException("no Courses or CoreCourses have been assigned, so no view can be shown");
            }
            return showSemesterView(semesterId, firstCoreCourse.getId());
        }
    }

    // actual Mapping for semesterView after parameters have been defined
    @GetMapping("/semesterView/show")
    public ModelAndView showSemesterView(@RequestParam(name = "selectedSemester") Long semesterId,
            @RequestParam(name = "selectedCoreCourse") Long coreCourseId) throws NotFoundException {

        ModelAndView mav = new ModelAndView("studentTemplates/semesterViewShow");
        
        /** The following parts deal with time parameters */
        Semester semester = semesterRepo.findSemesterById(semesterId);
        mav.addObject("semester", semester);

        mav.addObject("allMondays", helper.getAllMondaysOfSemester(semester));

        // ** retrieve current user name */
        User user = helper.getCurrentUser();       
        mav.addObject("userName", user.getFirstName());

        List<Course> userCourses = new ArrayList<Course>(user.getCourses());
        // check if current user is a student
        if (helper.currentUserIsA("Student")) {
            // ** add all the subjects */
            mav.addObject("userSubjects", helper.getSubjectsOfAStudentUser(user));
        } else {
            /**
             * Else user is not a student. User will see the actual coreCourse subject list
             */
            CoreCourse coreCourse = coreCourseRepo.findCoreCourseById(coreCourseId);
            mav.addObject("userSubjects", helper.getAllSubjectsOfACoreCourse(coreCourse));
        }

        /** Create the user's CoreCourses for the Navbar List */
        List<CoreCourse> teacherStudentCoreCourses = user.getCourses().stream().map(Course::getUsersList)
                .flatMap(List::stream).distinct().filter(u -> Objects.nonNull(u.getCoreCourse()))
                .map(User::getCoreCourse).distinct().sorted((c1, c2) -> c1.getId().compareTo(c2.getId()))
                .collect(Collectors.toList());
        mav.addObject("teachersCoreCourses", teacherStudentCoreCourses);

        // Elaboration:
        // List <Course> userCourseList = new ArrayList<Course>(user.getCourses());

        // List<User> userStudentList =
        // userCourseList.stream().map(Course::getUsersList).flatMap(List::stream).distinct().collect(Collectors.toList());

        // List<CoreCourse> teacherStudentCoreCourses =
        // userStudentList.stream().filter(u ->
        // Objects.nonNull(u.getCoreCourse())).map(User::getCoreCourse).distinct().sorted((c1,
        // c2) -> c1.getId().compareTo(c2.getId())).collect(Collectors.toList());

        /** The following parts deal with exam information */
        /**
         * retrieve allExams, week by week, for the whole semester and put into a list
         * of hashmap. note: dead approach with list of list leads to thymeleaf
         * useability restrictions dead code: List<List<Exam>> allExams = new
         * ArrayList<>();
         */
        monday = semesterStart;
        List<HashMap<String, Exam>> allExams = new ArrayList<>();
        if (user.getRoles().contains(roleRepo.findRoleById(39l))) {
            while (monday.isBefore(semesterEnd)) {
                List<Exam> examsByWeek = examRepo.findAllByDueDateBetween(monday, monday.with(DayOfWeek.SUNDAY))
                        .stream().filter(e -> userCourses.contains(e.getCourse())).collect(Collectors.toList());
                HashMap<String, Exam> map = new HashMap<>();
                for (Exam exam : examsByWeek) {
                    map.put(exam.getCourse().getSubject().getTag(), exam);
                }
                allExams.add(map);
                monday = monday.plusWeeks(1);
            }
        } else {
            CoreCourse coreCourse = coreCourseRepo.findCoreCourseById(coreCourseId);
            List<Course> coreCourseCourses = coreCourse.getStudents().stream()
                    .filter(u -> Objects.nonNull(u.getCoreCourse())).map(User::getCoursesList).flatMap(List::stream)
                    .distinct().collect(Collectors.toList());
            while (monday.isBefore(semesterEnd)) {
                List<Exam> examsByWeek = examRepo.findAllByDueDateBetween(monday, monday.with(DayOfWeek.SUNDAY))
                        .stream().filter(e -> coreCourseCourses.contains(e.getCourse())).collect(Collectors.toList());
                HashMap<String, Exam> map = new HashMap<>();
                for (Exam exam : examsByWeek) {
                    map.put(exam.getCourse().getSubject().getTag(), exam);
                }
                allExams.add(map);
                monday = monday.plusWeeks(1);
            }
        }
        mav.addObject("allExams", allExams);

        /** create Hashmap mapping every single day of the holiday with holiday */
        monday = semesterStart;
        List<Holiday> listHolidays = holidayRepo.findAllByStartDateBetween(semesterStart, semesterEnd);
        HashMap<LocalDate, Holiday> alleTageMap = new HashMap<>();
        for (Holiday holiday : listHolidays) {
            long holidayLength = ChronoUnit.DAYS.between(holiday.getStartDate(), holiday.getEndDate());
            List<LocalDate> allHolidayDays = Stream.iterate(holiday.getStartDate(), date -> date.plusDays(1))
                    .limit(holidayLength + 1).collect(Collectors.toList());
            for (LocalDate date : allHolidayDays) {

                alleTageMap.put(date, holiday);
            }
        }

        mav.addObject("alleTageMap", alleTageMap);

        List<Subject> allSubjects = subjectRepo.findAll();
        mav.addObject("allSubjects", allSubjects);
        // count all the subjects, method returns "numberOfSubjects"
        helper.calculateNumberOfSubjects(mav, allSubjects);

        /** The following parts deal with exam information */

        return mav;

    }

    @GetMapping("/semesterView/choose")
    public ModelAndView selectSemester() {
        ModelAndView mav = new ModelAndView("studentTemplates/semesterViewChoose");
        List<Semester> allSemesters = semesterRepo.findAll();
        mav.addObject("allSemesters", allSemesters);

        Operator semester = new Operator();
        mav.addObject("chosenSemster", semester);

        return mav;
    }

    /**
     * Sample Code
     * 
     * // Sample code 1: How to create a Holiday Hashmap // monday = semesterStart;
     * // List<Holiday> allHolidays =
     * holidayRepo.findAllByStartDateBetween(semesterStart, semesterEnd); //
     * mav.addObject("allHolidays", allHolidays); // HashMap<LocalDate, Holiday>
     * allHolidaysMap = new HashMap<>(); // for (Holiday holiday : allHolidays) {
     * 
     * // allHolidaysMap.put(holiday.getStartDate(), holiday); // } //
     * mav.addObject("allHolidaysMap", allHolidaysMap);
     * 
     * // Sample code 2: How to create a List containing all the days of all the
     * holidays // this allows catching holidays that are wrongly set, or begin
     * mid-week -> // TemporalAdjusters then sets start to following Monday. //
     * List<Holiday> allHolidaysList =
     * holidayRepo.findAllByStartDateBetween(semesterStart, semesterEnd); //
     * List<LocalDate> allHolidaysDays = new ArrayList<>(); // for (Holiday holiday
     * : allHolidaysList) { // long numOfDays = ChronoUnit.DAYS.between( //
     * holiday.getStartDate().with(TemporalAdjusters.nextOrSame(DayOfWeek.MONDAY)),
     * holiday.getEndDate()); // List<LocalDate> allDays =
     * Stream.iterate(holiday.getStartDate(), date ->
     * date.plusDays(1)).limit(numOfDays) // .collect(Collectors.toList()); //
     * allHolidaysDays.addAll(allDays); // } // mav.addObject("allHolidaysList",
     * allHolidaysDays);
     * 
     * /** Hilfsmethoden
     * 
     */

}
