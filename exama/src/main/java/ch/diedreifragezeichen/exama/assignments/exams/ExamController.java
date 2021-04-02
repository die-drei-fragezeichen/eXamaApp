package ch.diedreifragezeichen.exama.assignments.exams;

import java.time.*;
import java.util.*;
import java.util.stream.Collectors;

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
import ch.diedreifragezeichen.exama.assignments.workloadDistributions.WorkloadDistribution;
import ch.diedreifragezeichen.exama.assignments.workloadDistributions.WorkloadDistributionRepository;
import ch.diedreifragezeichen.exama.courses.Course;
import ch.diedreifragezeichen.exama.courses.CourseRepository;
import ch.diedreifragezeichen.exama.semesters.Semester;
import ch.diedreifragezeichen.exama.semesters.SemesterRepository;
import ch.diedreifragezeichen.exama.users.User;
import ch.diedreifragezeichen.exama.users.UserRepository;

@Controller
public class ExamController {
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
        LocalDate lastDay = exam.getSemester().getEndDate();
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
        LocalDate lastDay = exam.getSemester().getEndDate();
        mav.addObject("lastDay", lastDay);
        return mav;
    }

    /**
     * Different redirects after exam created
     */

    // Postmapping for creation/update from exam overview page
    @PostMapping("/exams/modified-overview")
    @Transactional
    public String modifyOverview(Exam exam) {
        Authentication authLoggedInUser = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepo.findUserByEmail(authLoggedInUser.getName());
        exam.setCreator(user);
        exam.setEditDate(LocalDate.now());
        if(exam.getWorkloadMinutesTotal()==null){
            exam.setWorkloadMinutesTotal(0.0);
        }
        em.unwrap(org.hibernate.Session.class).saveOrUpdate(exam);
        return "redirect:/exams/show";
    }

    // Postmapping for creation from teacher landing
    @PostMapping("/exams/modified-start")
    @Transactional
    public ModelAndView modify(Exam exam) {
        ModelAndView mav = new ModelAndView("teacherTemplates/index");
        Authentication authLoggedInUser = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepo.findUserByEmail(authLoggedInUser.getName());
        exam.setCreator(user);
        exam.setEditDate(LocalDate.now());
        if(exam.getWorkloadMinutesTotal()==null){
            exam.setWorkloadMinutesTotal(0.0);
        }
        em.unwrap(org.hibernate.Session.class).saveOrUpdate(exam);
        mav.addObject("createdExam", exam);
        return mav;
    }

    // Postmapping for creation from semesterview
    @PostMapping("/exams/modified-semesterview")
    @Transactional
    public String modifySemesterview(Exam exam) {
        Authentication authLoggedInUser = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepo.findUserByEmail(authLoggedInUser.getName());
        exam.setCreator(user);
        exam.setEditDate(LocalDate.now());
        if(exam.getWorkloadMinutesTotal()==null){
            exam.setWorkloadMinutesTotal(0.0);
        }
        em.unwrap(org.hibernate.Session.class).saveOrUpdate(exam);
        return "redirect:/exams/show";
    }

    @GetMapping("/exams/delete")
    public String delete(@RequestParam(name = "id") Long id) {
        examRepo.deleteById(id);
        return "redirect:/exams/show";
    }

    // /*
    //  * These parts handle the examBar /*
    //  * 
    //  * Arriving at this path creates a mav bean that is filled with a link and a
    //  * datum object that can now be filled on HTML by user
    //  */
    // @GetMapping("/examBar/selectDate")
    // public ModelAndView selectExamBarDate() {

    //     Operator datum = new Operator();
    //     ModelAndView mav = new ModelAndView("studentTemplates/examBarSelectDate");
    //     mav.addObject("datum", datum);
    //     return mav;
    // }

    // /**
    //  * When User click submit button, this method is execuded. Te datum object is
    //  * saved into db by operatorRepo o note that this link is different from above,
    //  * but it is never really shown, user is directly redirected.
    //  */
    // @PostMapping("/examBar/DateSelected")
    // public String processSelectedDate(Operator datum) {
    //     operatorRepo.save(datum);
    //     return "redirect:/examBar/show";
    // }

    // /**
    //  * Fetches All exams based on the selected Datum object
    //  */
    // @RequestMapping("/examBar/show")
    // public String showExamBarForSavedDate(Model model) {
    //     Operator selectedDatum = operatorRepo.findAll().get(0);
    //     operatorRepo.deleteAll();
    //     LocalDate selectedDate = selectedDatum.getSelectedDate();
    //     model.addAttribute("Datum", selectedDate);

    //     ExamService helper = new ExamService();
    //     LocalDate Monday = helper.getFirstDayOfWeek(selectedDate);
    //     model.addAttribute("Montag", Monday);

    //     LocalDate Sunday = helper.getLastDayOfWeek(selectedDate);
    //     model.addAttribute("Sonntag", Sunday);

    //     List<Exam> listExams = examRepo.findAllByDueDateBetween(Monday, Sunday);
    //     model.addAttribute("liste", listExams);

    //     calculateNumberOfExams(model, listExams);
    //     calculateExamFactor(model, listExams);

    //     return "studentTemplates/examBarShow";

    // }

    // /**
    //  * Hilfsmethode 1
    //  */

    // public Model calculateNumberOfExams(Model model, List<Exam> listExams) {
    //     // NOTE: Thymeleaf isn't Velocity or Freemarker and doesn't replace expressions
    //     // blindly. You need the expression in an appropriate tag attribute, such as
    //     // <h1 data-th-text="${data}" />

    //     model.addAttribute("msg", "Anzahl Leistungsmessungen diese Woche: ");
    //     long anzahlExamen = listExams.stream().count();
    //     model.addAttribute("anzpr", anzahlExamen);
    //     // Schreibe im html: <h1 data-th-text="${anzpr}" />
    //     return model;
    // }

    // /**
    //  * Hilfsmethode 2
    //  */

    // public Model calculateExamFactor(Model model, List<Exam> listExams) {

    //     model.addAttribute("msg2", "Insgesamt zählt das mit einem Belastungsfaktor von: ");
    //     double ExamFactor = listExams.stream().mapToDouble(exam -> exam.getCountingFactor()).sum();
    //     model.addAttribute("xFactor", ExamFactor);
    //     // Schreibe im html: <h1 data-th-text="${anzpr}" />
    //     return model;
    // }

    // /**
    //  * Hilfsmethode 3
    //  */

    // public ModelAndView calculateNumberOfSubjects(ModelAndView mav, List<Subject> allSubjects) {
    //     long numberOfSubjects = allSubjects.stream().count();
    //     mav.addObject("numberOfSubjects", numberOfSubjects);
    //     return mav;
    // }
}