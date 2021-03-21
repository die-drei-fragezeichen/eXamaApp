package ch.diedreifragezeichen.exama.assignments.exams;

import java.time.*;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.persistence.*;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import ch.diedreifragezeichen.exama.assignments.availablePrepTimes.AvailablePrepTime;
import ch.diedreifragezeichen.exama.assignments.availablePrepTimes.AvailablePrepTimeRepository;
import ch.diedreifragezeichen.exama.assignments.examTypes.ExamType;
import ch.diedreifragezeichen.exama.assignments.examTypes.ExamTypeRepository;
import ch.diedreifragezeichen.exama.assignments.workload.WorkloadDistribution;
import ch.diedreifragezeichen.exama.assignments.workload.WorkloadDistributionRepository;
import ch.diedreifragezeichen.exama.courses.Course;
import ch.diedreifragezeichen.exama.courses.CourseRepository;
import ch.diedreifragezeichen.exama.operator.Operator;
import ch.diedreifragezeichen.exama.operator.OperatorRepository;
import ch.diedreifragezeichen.exama.semesters.Holiday;
import ch.diedreifragezeichen.exama.semesters.HolidayRepository;
import ch.diedreifragezeichen.exama.semesters.Semester;
import ch.diedreifragezeichen.exama.semesters.SemesterRepository;
import ch.diedreifragezeichen.exama.subjects.Subject;
import ch.diedreifragezeichen.exama.subjects.SubjectRepository;
import ch.diedreifragezeichen.exama.users.User;
import ch.diedreifragezeichen.exama.users.UserRepository;
import javassist.NotFoundException;

@Controller
public class ExamController {
    @Autowired
    private ExamRepository examRepo;

    @Autowired
    private ExamTypeRepository examtypeRepo;

    @Autowired
    private AvailablePrepTimeRepository availablePrepTimeRepo;

    @Autowired
    private SubjectRepository subjectRepo;

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

    @Autowired
    private HolidayRepository holidayRepo;

    @PersistenceContext
    private EntityManager em;

    /**
     * Exam Mappings
     */

    @GetMapping("/exams/show")
    public String show(Model model) {
        List<Exam> listExams = examRepo.findAll();
        model.addAttribute("allExams", listExams);
        return "teacherTemplates/examsShow";
    }

    @GetMapping("/exams/create")
    public ModelAndView chooseSemester(Model model) {
        ModelAndView mav = new ModelAndView("teacherTemplates/examCreateChooseSemester");
        List<Semester> semesterList = semesterRepo.findAll();
        semesterList.stream().filter(s -> s.isEnabled()).collect(Collectors.toList());
        semesterList.sort(Comparator.comparing(Semester::getStartDate));
        mav.addObject("allSemesters", semesterList);
        Exam exam = new Exam();
        mav.addObject("newExam", exam);
        return mav;
    }

    @PostMapping("/exams/create")
    public ModelAndView add(Exam exam) {
        Authentication authLoggedInUser = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepo.findUserByEmail(authLoggedInUser.getName());
        ModelAndView mav = new ModelAndView("teacherTemplates/examModify");
        mav.addObject("exam", exam);
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
        LocalDate firstDay = exam.getSemester().getStartDate();
        mav.addObject("firstDay", firstDay);
        LocalDate lastDay = exam.getSemester().getStartDate();
        mav.addObject("lastDay", lastDay);
        return mav;
    }

    @GetMapping("/exams/edit")
    public ModelAndView edit(@RequestParam(name = "id") Long id) {
        Authentication authLoggedInUser = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepo.findUserByEmail(authLoggedInUser.getName());
        ModelAndView mav = new ModelAndView("teacherTemplates/examModify");
        Exam exam = examRepo.findExamById(id);
        mav.addObject("exam", exam);
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
        LocalDate firstDay = exam.getSemester().getStartDate();
        mav.addObject("firstDay", firstDay);
        LocalDate lastDay = exam.getSemester().getStartDate();
        mav.addObject("lastDay", lastDay);
        return mav;
    }

    @PostMapping("/exams/modified")
    @Transactional
    public String modify(Exam exam) {
        Authentication authLoggedInUser = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepo.findUserByEmail(authLoggedInUser.getName());
        exam.setCreator(user);
        exam.setEditDate(LocalDate.now());
        em.unwrap(org.hibernate.Session.class).saveOrUpdate(exam);
        return "redirect:/exams/show";
    }

    @GetMapping("/exams/delete")
    public String delete(@RequestParam(name = "id") Long id) {
        examRepo.deleteById(id);
        return "redirect:/exams/show";
    }

    /*
     * These parts handle the examBar /*
     * 
     * Arriving at this path creates a mav bean that is filled with a link and a
     * datum object that can now be filled on HTML by user
     */
    @GetMapping("/examBar/selectDate")
    public ModelAndView selectExamBarDate() {

        Operator datum = new Operator();
        ModelAndView mav = new ModelAndView("studentTemplates/examBarSelectDate");
        mav.addObject("datum", datum);
        return mav;
    }

    /**
     * When User click submit button, this method is execuded. Te datum object is
     * saved into db by operatorRepo o note that this link is different from above,
     * but it is never really shown, user is directly redirected.
     */
    @PostMapping("/examBar/DateSelected")
    public String processSelectedDate(Operator datum) {
        operatorRepo.save(datum);
        return "redirect:/examBar/show";
    }

    /**
     * Fetches All exams based on the selected Datum object
     */
    @RequestMapping("/examBar/show")
    public String showExamBarForSavedDate(Model model) {
        Operator selectedDatum = operatorRepo.findAll().get(0);
        operatorRepo.deleteAll();
        LocalDate selectedDate = selectedDatum.getSelectedDate();
        model.addAttribute("Datum", selectedDate);

        ExamService helper = new ExamService();
        LocalDate Monday = helper.getFirstDayOfWeek(selectedDate);
        model.addAttribute("Montag", Monday);

        LocalDate Sunday = helper.getLastDayOfWeek(selectedDate);
        model.addAttribute("Sonntag", Sunday);

        List<Exam> listExams = examRepo.findAllByDueDateBetween(Monday, Sunday);
        model.addAttribute("liste", listExams);

        calculateNumberOfExams(model, listExams);
        calculateExamFactor(model, listExams);

        return "studentTemplates/examBarShow";

    }

    /**
     * Hilfsmethode 1
     */

    public Model calculateNumberOfExams(Model model, List<Exam> listExams) {
        // NOTE: Thymeleaf isn't Velocity or Freemarker and doesn't replace expressions
        // blindly. You need the expression in an appropriate tag attribute, such as
        // <h1 data-th-text="${data}" />

        model.addAttribute("msg", "Anzahl Leistungsmessungen diese Woche: ");
        long anzahlExamen = listExams.stream().count();
        model.addAttribute("anzpr", anzahlExamen);
        // Schreibe im html: <h1 data-th-text="${anzpr}" />
        return model;
    }

    /**
     * Hilfsmethode 2
     */

    public Model calculateExamFactor(Model model, List<Exam> listExams) {

        model.addAttribute("msg2", "Insgesamt zählt das mit einem Belastungsfaktor von: ");
        double ExamFactor = listExams.stream().mapToDouble(exam -> exam.getCountingFactor()).sum();
        model.addAttribute("xFactor", ExamFactor);
        // Schreibe im html: <h1 data-th-text="${anzpr}" />
        return model;
    }

    /**
     * Hilfsmethode 3
     */

    public ModelAndView calculateNumberOfSubjects(ModelAndView mav, List<Subject> allSubjects) {
        long numberOfSubjects = allSubjects.stream().count();
        mav.addObject("numberOfSubjects", numberOfSubjects);
        return mav;
    }

    /**
     * The following methods handle the Semester view creation
     */

    @GetMapping("/semesterView/choose")
    public ModelAndView selectSemester() {
        ModelAndView mav = new ModelAndView("studentTemplates/semesterViewChoose");
        List<Semester> allSemesters = semesterRepo.findAll();
        mav.addObject("allSemesters", allSemesters);

        Operator semester = new Operator();
        mav.addObject("chosenSemster", semester);

        return mav;
    }

    /*
     * Not necessary anymore
     * 
     * @PostMapping("/semesterView/selected") public String
     * processSelectedSemester(Operator semester) { operatorRepo.save(semester);
     * return "redirect:/semesterView/show"; }
     */

    @GetMapping("/semesterView/show")
    public ModelAndView showSemesterView(@RequestParam(name = "selectedSemester") Long id) throws NotFoundException {
        ModelAndView mav = new ModelAndView("studentTemplates/semesterViewShow");
        // retrieve selected semester
        // Operator selected = operatorRepo.findAll().get(0);
        // operatorRepo.deleteAll();
        // Semester semester = selected.getSelectedSemester();
        Semester semester = semesterRepo.findSemesterById(id);
        mav.addObject("semester", semester);
        // retrieve semester Information and first / last day of Semester
        LocalDate semesterStart = semester.getStartDate().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDate monday = semesterStart;
        LocalDate semesterEnd = semester.getEndDate().with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));
        // count all the exams
        Long examTotal = examRepo.findAllByDueDateBetween(semesterStart, semesterEnd).stream().count();
        mav.addObject("examTotal", examTotal);
        // create a list of all Mondays
        List<LocalDate> mondays = new ArrayList<>();
        while (monday.isBefore(semesterEnd)) {
            mondays.add(monday);
            // Set up the loop.
            monday = monday.plusWeeks(1);
        }
        mav.addObject("allMondays", mondays);

        // retrieve allExams, week by week, for the whole semester
        // dead approach with list of list leads to thymeleaf useability restrictions
        // dead code: List<List<Exam>> allExams = new ArrayList<>();i
        // working approach: creating a list of hashmaps --> usage see html.
        monday = semesterStart;
        List<HashMap<String, Exam>> allExams = new ArrayList<>();
        while (monday.isBefore(semesterEnd)) {
            List<Exam> examsByWeek = examRepo.findAllByDueDateBetween(monday, monday.with(DayOfWeek.SUNDAY));
            HashMap<String, Exam> map = new HashMap<>();
            for (Exam exam : examsByWeek) {
                map.put(exam.getCourse().getSubject().getTag(), exam);
            }
            allExams.add(map);
            monday = monday.plusWeeks(1);
        }
        mav.addObject("allExams", allExams);
        long xExam = allExams.stream().count();
        mav.addObject("xExam", xExam);

        // create a HolidayHashmap
        // not used because finding all days between is easier in list: see below
        monday = semesterStart;
        List<Holiday> allHolidays = holidayRepo.findAllByStartDateBetween(semesterStart, semesterEnd);
        mav.addObject("allHolidays", allHolidays);
        HashMap<LocalDate, Holiday> allHolidaysMap = new HashMap<>();
        for (Holiday holiday : allHolidays) {

            allHolidaysMap.put(holiday.getStartDate(), holiday);
        }
        mav.addObject("allHolidaysMap", allHolidaysMap);

        // create a List containing all the days of all the holidays
        // this allows catching holidays that are wrongly set, or begin mid-week -> TemporalAdjusters then sets start to following Monday.
        List<Holiday> allHolidaysList = holidayRepo.findAllByStartDateBetween(semesterStart, semesterEnd);
        List<LocalDate> allHolidaysDays = new ArrayList<>();
        for (Holiday holiday : allHolidaysList) {
            long numOfDays = ChronoUnit.DAYS.between(holiday.getStartDate().with(TemporalAdjusters.nextOrSame(DayOfWeek.MONDAY)), holiday.getEndDate());
            List<LocalDate> allDays = Stream.iterate(holiday.getStartDate(), date -> date.plusDays(1))
                    .limit(numOfDays).collect(Collectors.toList());
            allHolidaysDays.addAll(allDays);
        }
        mav.addObject("allHolidaysList", allHolidaysDays);



        

        List<Subject> allSubjects = subjectRepo.findAll();
        mav.addObject("allSubjects", allSubjects);
        // count all the subjects, method returns "numberOfSubjects"
        calculateNumberOfSubjects(mav, allSubjects);
        return mav;



    }

    /*
     * Hilfsmethode 4
     */
    @SuppressWarnings("unused")
    private int calculateNumberOfExams(List<List<Exam>> allExams) {
        int sum = 0;
        Iterator<List<Exam>> iter = allExams.iterator();
        while (iter.hasNext()) {
            Iterator<Exam> innerIter = iter.next().iterator();
            while (innerIter.hasNext()) {
                sum++;
            }
        }
        return sum;
    }
}