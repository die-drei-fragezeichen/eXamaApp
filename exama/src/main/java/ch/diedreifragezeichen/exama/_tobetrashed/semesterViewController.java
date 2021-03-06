// package ch.diedreifragezeichen.exama._tobetrashed;

// import java.time.*;
// import java.util.*;
// import java.util.stream.Collectors;

// import javax.persistence.EntityManager;
// import javax.persistence.PersistenceContext;

// import org.springframework.beans.factory.annotation.*;

// import org.springframework.stereotype.Controller;
// import org.springframework.web.bind.annotation.*;
// import org.springframework.web.servlet.ModelAndView;

// import ch.diedreifragezeichen.exama._services.AppService;
// import ch.diedreifragezeichen.exama.assignments.assignment.Assignment;
// import ch.diedreifragezeichen.exama.assignments.availablePrepTimes.AvailablePrepTime;
// import ch.diedreifragezeichen.exama.assignments.availablePrepTimes.AvailablePrepTimeRepository;
// import ch.diedreifragezeichen.exama.assignments.examTypes.ExamType;
// import ch.diedreifragezeichen.exama.assignments.examTypes.ExamTypeRepository;
// import ch.diedreifragezeichen.exama.assignments.exams.*;
// import ch.diedreifragezeichen.exama.assignments.homeworks.Homework;
// import ch.diedreifragezeichen.exama.assignments.workloadDistributions.WorkloadDistribution;
// import ch.diedreifragezeichen.exama.assignments.workloadDistributions.WorkloadDistributionRepository;
// import ch.diedreifragezeichen.exama.courses.CoreCourse;
// import ch.diedreifragezeichen.exama.courses.CoreCourseRepository;
// import ch.diedreifragezeichen.exama.courses.Course;
// import ch.diedreifragezeichen.exama.courses.CourseRepository;
// import ch.diedreifragezeichen.exama.operator.*;
// import ch.diedreifragezeichen.exama.semesters.*;
// import ch.diedreifragezeichen.exama.users.User;

// import javassist.NotFoundException;

// @Controller
// public class semesterViewController {
//     @Autowired
//     private SemesterRepository semesterRepo;

//     @Autowired
//     private HolidayRepository holidayRepo;

//     @Autowired
//     private CoreCourseRepository coreCourseRepo;

//     @Autowired
//     private CourseRepository courseRepo;

//     @Autowired
//     private ExamTypeRepository examtypeRepo;

//     @Autowired
//     private AvailablePrepTimeRepository availablePrepTimeRepo;

//     @PersistenceContext
//     private EntityManager em;

//     @Autowired
//     private AppService helper;

//     @Autowired
//     private WorkloadDistributionRepository distributionRepo;

//     /**
//      * The following methods handle the Semester view creation
//      * 
//      * @throws NotFoundException
//      */

//     @GetMapping("/semesterView")
//     public ModelAndView initializeView() throws NotFoundException {

//         Semester currentSemester = helper.getCurrentSemesterBasedOnDate(LocalDate.now());

//         return selectSemesterView(currentSemester.getId());
//     }

//     // mapping following semester selection
//     @GetMapping("/semesterView/select")
//     public ModelAndView selectSemesterView(@RequestParam(name = "selectedSemester") Long semesterId)
//             throws NotFoundException {
//         // get current User
//         User user = helper.getCurrentUser();
//         // if user is student get coreCourse id and redirect accordingly
//         if (helper.currentUserIsA("Student")) {
//             CoreCourse userCoreCourse = user.getCoreCourse();
//             if (userCoreCourse == null) {
//                 throw new NotFoundException("CoreCourse not assigned");
//             }
//             return showSemesterView(semesterId, userCoreCourse.getId());

//         } else {
//             /**
//              * Else user is not a student. User will see the actual coreCourse subject list.
//              * So user is redirected with its first coreCourse
//              */
//             CoreCourse firstCoreCourse = user.getCourses().stream().map(Course::getUsersList).flatMap(List::stream)
//                     .distinct().filter(u -> Objects.nonNull(u.getCoreCourse())).map(User::getCoreCourse).distinct()
//                     .sorted((c1, c2) -> c1.getId().compareTo(c2.getId())).findFirst().orElse(null);
//             if (firstCoreCourse == null) {
//                 throw new NotFoundException("no Courses or CoreCourses have been assigned, so no view can be shown");
//             }
//             return showSemesterView(semesterId, firstCoreCourse.getId());
//         }
//     }

//     // actual Mapping for semesterView after parameters have been defined
//     @GetMapping("/semesterView/show")
//     public ModelAndView showSemesterView(@RequestParam(name = "selectedSemester") Long semesterId,
//             @RequestParam(name = "selectedCoreCourse") Long coreCourseId) throws NotFoundException {
        
//         ModelAndView mav;
//         if (helper.currentUserIsA("Student")) {
//             mav = new ModelAndView("studentTemplates/semesterViewShow");
//         } else {
//             mav = new ModelAndView("teacherTemplates/semesterViewShow");
//         }
//         /** The following parts deal with time parameters */
//         Semester chosenSemester = semesterRepo.findSemesterById(semesterId);
//         mav.addObject("semester", chosenSemester);

//         List<LocalDate> allMondays = helper.getAllMondaysOfSemester(chosenSemester);
//         mav.addObject("allMondays", allMondays);

//         // ** retrieve current user name */
//         User user = helper.getCurrentUser();
//         mav.addObject("userName", user.getFirstName());

//         List<Course> userCourses = new ArrayList<Course>(user.getCourses());
//         // check if current user is a student
//         if (helper.currentUserIsA("Student")) {
//             // ** add all the subjects */
//             mav.addObject("userSubjects", helper.getSubjectsOfAStudentUser());
//         } else {
//             /**
//              * Else user is not a student. Teacher will see the actual coreCourse subject
//              * list
//              */
//             mav.addObject("userSubjects",
//                     helper.getAllSubjectsOfACoreCourse(coreCourseRepo.findCoreCourseById(coreCourseId)));
//         }

//         /** Add user's CoreCourses for the Navbar List */
//         mav.addObject("teachersCoreCourses", helper.getAllTeacherStudentCoreCourses());

//         /** The following parts deal with exam information */
//         /**
//          * retrieve allExams, week by week, for the whole semester and put into a list
//          * of hashmap. note: dead approach with list of list leads to thymeleaf
//          * useability restrictions dead code: List<List<Exam>> allExams = new
//          * ArrayList<>();
//          */
//         LocalDate semesterStart = chosenSemester.getStartDate();
//         LocalDate semesterEnd = chosenSemester.getEndDate();
//         LocalDate monday = semesterStart;
//         List<HashMap<String, Exam>> allExams = new ArrayList<>();
//         if (helper.currentUserIsA("Student")) {
//             while (monday.isBefore(semesterEnd)) {
//                 List<Exam> examsByWeek = helper.getExamsForSevenDaysList(userCourses, monday);
//                 HashMap<String, Exam> map = new HashMap<>();
//                 for (Exam exam : examsByWeek) {
//                     map.put(exam.getCourse().getSubject().getTag(), exam);
//                 }
//                 allExams.add(map);
//                 monday = monday.plusWeeks(1);
//             }
//         } else { // user is teacher or above
//             CoreCourse coreCourse = coreCourseRepo.findCoreCourseById(coreCourseId);
//             List<Course> coreCourseCourses = helper.getAllCoursesOfACoreCourse(coreCourse);
//             while (monday.isBefore(semesterEnd.plusDays(1))) {
//                 List<Exam> examsByWeek = helper.getExamsForSevenDaysList(coreCourseCourses, monday);
//                 HashMap<String, Exam> map = new HashMap<>();
//                 for (Exam exam : examsByWeek) {
//                     map.put(exam.getCourse().getSubject().getTag(), exam);
//                 }
//                 allExams.add(map);
//                 monday = monday.plusWeeks(1);
//             }
//         }
//         mav.addObject("allExams", allExams);

//         /** create Hashmap mapping every single day of the holiday with holiday */
//         monday = semesterStart;
//         List<Holiday> listHolidays = holidayRepo.findAllByStartDateBetween(semesterStart, semesterEnd);
//         HashMap<LocalDate, Holiday> allHolidayDates = new HashMap<>();
//         for (Holiday holiday : listHolidays) {
//             List<LocalDate> allHolyDates = helper.getAllDatesBetweenAndWith(holiday.getStartDate(),
//                     holiday.getEndDate());
//             for (LocalDate date : allHolyDates) {
//                 allHolidayDates.put(date, holiday);
//             }
//         }

//         mav.addObject("allHolidayDates", allHolidayDates);

//         // get a list of all the weekly workloads for every week
//         List<Double> weeklyWorkload = helper.getSemesterWorkloadList(coreCourseId, semesterId);
//         mav.addObject("weeklyWorkload", weeklyWorkload);

//         Exam exam = new Exam();
//         mav.addObject("exam", exam);

        
//         Homework homework = new Homework();
//         mav.addObject("homework", homework);

//         Semester semester = semesterRepo.findAll().stream().filter(s -> s.getStartDate().isBefore(LocalDate.now()) && s.getEndDate().isAfter(LocalDate.now())).collect(Collectors.toList()).get(0);
//         exam.setSemester(semester);

//         List<Course> listCourses = courseRepo.findAll();
//         List<Course> usersCourses = listCourses.stream().filter(c -> c.getUsers().contains(user))
//                 .collect(Collectors.toList());
//         mav.addObject("allCourses", usersCourses);

//         List<ExamType> listTypes = examtypeRepo.findAll();
//         mav.addObject("allExamTypes", listTypes);

//         List<AvailablePrepTime> listPrepTimes = availablePrepTimeRepo.findAll();
//         mav.addObject("allPrepTimes", listPrepTimes);

//         List<WorkloadDistribution> listDist = distributionRepo.findAll();
//         mav.addObject("allWorkloadDistributions", listDist);


//         return mav;

//     }

//     @GetMapping("/semesterView/choose")
//     public ModelAndView selectSemester() {
//         ModelAndView mav = new ModelAndView("studentTemplates/semesterViewChoose");
//         List<Semester> allSemesters = semesterRepo.findAll();
//         mav.addObject("allSemesters", allSemesters);

//         Operator semester = new Operator();
//         mav.addObject("chosenSemster", semester);

//         return mav;
//     }
// }
