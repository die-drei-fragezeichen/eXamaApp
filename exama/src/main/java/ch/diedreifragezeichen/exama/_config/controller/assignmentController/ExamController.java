package ch.diedreifragezeichen.exama._config.controller.assignmentController;

import java.time.LocalDate;
import java.util.List;

//import javassist.NotFoundException;

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
import ch.diedreifragezeichen.exama.assignments.exams.Exam;
import ch.diedreifragezeichen.exama.assignments.exams.ExamRepository;
import ch.diedreifragezeichen.exama.assignments.exams.ExamService;
import ch.diedreifragezeichen.exama.assignments.workload.WorkloadDistribution;
import ch.diedreifragezeichen.exama.assignments.workload.WorkloadDistributionRepository;
import ch.diedreifragezeichen.exama.courses.Course;
import ch.diedreifragezeichen.exama.courses.CourseRepository;
import ch.diedreifragezeichen.exama.subjects.Subject;
import ch.diedreifragezeichen.exama.subjects.SubjectRepository;
import ch.diedreifragezeichen.exama.userAdministration.User;
import ch.diedreifragezeichen.exama.userAdministration.UserRepository;

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
    private DatumRepository datumRepo;

    /**
     * The following methods handle the examBar creation
     */

    /**
     * user arriving at this path is enabled to create a new mime. When arriving at
     * the path a ModelAndView is created and the HTML link injected. New Mime
     * "template" is added to the ModelAndView and returned to the "HTML" to be
     * "filled"
     */

    @GetMapping("/exams/create")
    public ModelAndView newExam() {
        Exam exam = new Exam();
        ModelAndView mav = new ModelAndView("teacherTemplates/examsCreate");
        mav.addObject("newExam", exam);
        // NOTE: "verpacktesObjekt" is the so-called "NAME OF THE MODEL ATTRIBUTE"
        // mav.addAttribute("standardDate", new LocalDate());
        List<ExamType> listTypes = examtypeRepo.findAll();
        mav.addObject("holmirdietypenverpackung", listTypes);

        List<AvailablePrepTime> listPrepTimes = availablePrepTimeRepo.findAll();
        mav.addObject("bringmirdiepreptimes", listPrepTimes);

        List<Subject> listSubjects = subjectRepo.findAll();
        mav.addObject("welchefaecherhabenwir", listSubjects);

        List<Course> listKlassen = courseRepo.findAll();
        mav.addObject("holmirmaldiearmenschafe", listKlassen);

        List<WorkloadDistribution> listDist = distributionRepo.findAll();
        mav.addObject("allWorkloadDistributions", listDist);

        return mav;
    }

    /**
     * When clicking the submit button, the User sends an Mime object. The mimeRepo
     * saves this object into the db note that the link here is different than in
     * the get method, but the user never really sees it user is redirected to the
     * "show" page immediately
     */

    @PostMapping("/exams/created")
    public String processSaving(Exam exam) {
        Authentication authLoggedInUser = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepo.getUserByEmail(authLoggedInUser.getName());
        exam.setCreator(user);
        exam.setEditDate(LocalDate.now());
        exam.setExamType(examtypeRepo.findExamTypeById(1l));
        examRepo.save(exam);
        return "redirect:/exams/show";
    }

    /**
     * user arrives at exams/show and sees a list of all the mimes in DB table
     * mimes.
     */

    @GetMapping("/exams/show")
    public String showExams(Model model) {
        List<Exam> listExams = examRepo.findAll();
        model.addAttribute("allExams", listExams);
        return "teacherTemplates/examsShow";
    }

    /*
     * These parts handle the examBar /*
     * 
     * Arriving at this path creates a mav bean that is filled with a link and a
     * datum object that can now be filled on HTML by user
     */
    @GetMapping("/examBar/selectDate")
    public ModelAndView selectExamBarDate() {

        Datum datum = new Datum();
        ModelAndView mav = new ModelAndView("studentTemplates/examBarSelectDate");
        mav.addObject("datum", datum);
        return mav;
    }

    /**
     * When User click submit button, this method is execuded. Te datum object is
     * saved into db by datumRepo note that this link is different from above, but
     * it is never really shown, user is directly redirected.
     */
    @PostMapping("/examBar/DateSelected")
    public String processSelectedDate(Datum datum) {
        datumRepo.save(datum);
        return "redirect:/examBar/show";
    }

    /**
     * Fetches All exams based on the selected Datum object
     */
    @RequestMapping("/examBar/show")
    public String showExamBarForSavedDate(Model model) {
        Datum selectedDatum = datumRepo.findAll().get(0);
        datumRepo.deleteAll();
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

    // /**
    //  * The following methods handle the Semester view creation
    //  */
    
    // public ModelAndView selectExamBarDate() {

    //     Datum datum = new Datum();
    //     ModelAndView mav = new ModelAndView("studentTemplates/examBarSelectDate");
    //     mav.addObject("datum", datum);
    //     return mav;
    // }

    // @GetMapping("/examView/show")
    // public String showExamView() {
    //     ModelAndView mav = new ModelAndView("studentTemplates/examTableShow");
    //     List<Subject> allSubjects = subjectRepo.findAll();
    //     mav.addObject("alleFaecher", allSubjects);
        


    //     Datum selectedDatum = datumRepo.findAll().get(0);
    //     datumRepo.deleteAll();
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




}