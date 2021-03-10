package ch.diedreifragezeichen.exama._config.controller.assignmentController;

import java.time.LocalDate;
import java.util.List;

//import javassist.NotFoundException;

import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import ch.diedreifragezeichen.exama.assignments.examTypes.ExamType;
import ch.diedreifragezeichen.exama.assignments.examTypes.ExamTypeRepository;
import ch.diedreifragezeichen.exama.assignments.mime.Mime;
import ch.diedreifragezeichen.exama.assignments.mime.MimeRepository;
import ch.diedreifragezeichen.exama.TESTKLASSEN.datums.Datum;
import ch.diedreifragezeichen.exama.TESTKLASSEN.datums.DatumRepository;

@Controller
public class MimeController {
    @Autowired
    private MimeRepository mimeRepo;

    @Autowired
    private DatumRepository datumRepo;

    @Autowired
    private ExamTypeRepository examtypeRepo;

   
    /**
     * The following methods handle the mime Creation
     */

    /**
     * user arriving at this path is enabled to create a new exam. When arriving at
     * the path a ModelAndView is created and the HTML link injected. New Exam
     * "template" is added to the ModelAndView and returned to the "HTML" to be
     * "filled"
     */

    @GetMapping("/mimes/create")
    public ModelAndView newMime() {
        Mime mime = new Mime();
        ModelAndView mav = new ModelAndView("teacherTemplates/mimesCreate");
        mav.addObject("verpacktesObjekt", mime);
        // NOTE: "verpacktesObjekt" is the so-called "NAME OF THE MODEL ATTRIBUTE"
        // mav.addAttribute("standardDate", new LocalDate());
        List<ExamType> listTypes = examtypeRepo.findAll();
        mav.addObject("holmirdietypenverpackung", listTypes);

        return mav;
    }

    /**
     * When clicking the submit button, the User sends an Exam object. The examRepo
     * saves this object into the db note that the link here is different than in
     * the get method, but the user never really sees it user is redirected to the
     * "show" page immediately
     */

    @PostMapping("/mimes/created")
    public String processSaving(Mime mime) {
        mimeRepo.save(mime);
        return "redirect:/mimes/show";
    }

    /**
     * user arrives at exams/show and sees a list of all the exams in DB.
     */

    @GetMapping("/mimes/show")
    public String showMimes(Model model) {
        List<Mime> listMimes = mimeRepo.findAll();
        model.addAttribute("nunNimmDieseListe", listMimes);
        return "teacherTemplates/mimesShow";
    }

 /**
     * The following methods handle the examBar
     */

    /**
     * Arriving at this path creates a mav bean that is filled with a link and a
     * datum object that can now be filled on HTML by user
     */
    // @GetMapping("/examBar/selectDate")
    // public ModelAndView selectExamBarDate() {

    //     Datum datum = new Datum();
    //     ModelAndView mav = new ModelAndView("studentTemplates/examBarSelectDate");
    //     mav.addObject("datum", datum);
    //     return mav;
    // }

    /**
     * When User click submit button, this method is execuded. Te datum object is
     * saved into db by datumRepo note that this link is different from above, but
     * it is never really shown, user is directly redirected.
     */
    // @PostMapping("/examBar/DateSelected")
    // public String processSelectedDate(Datum datum) {
    //     datumRepo.save(datum);
    //     return "redirect:/examBar/show";
    // }

    // /**
    //  * Fetches All exams based on the selected Datum object
    //  */
    // @RequestMapping("/examBar/show")
    // public String showExamBarForSavedDate(Model model) {
    //     Datum selectedDatum = datumRepo.findAll().get(0);
    //     datumRepo.deleteAll();
    //     LocalDate selectedDate = selectedDatum.getSelectedDate();
    //     model.addAttribute("Datum", selectedDate);

    //     ExamService helper = new ExamService();
    //     LocalDate Monday = helper.getFirstDayOfWeek(selectedDate);
    //     model.addAttribute("Montag", Monday);

    //     LocalDate Sunday = helper.getLastDayOfWeek(selectedDate);
    //     model.addAttribute("Sonntag", Sunday);

    //     List<Exam> listExams = examRepo.findAllByDateBetween(Monday, Sunday);
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




    // @GetMapping("/exams/{id}/edit")
    // public ModelAndView editexam(@PathVariable(name = "id") Long id) throws
    // NotFoundException {
    // Exam exam = examRepo.findExamById(id);
    // if (exam == null) {
    // throw new NotFoundException("Exam not found");
    // }
    // ModelAndView mav = new ModelAndView("adminTemplates/examEdit");
    // mav.addObject("exam", exam);
    // return mav;
    // }

    // @GetMapping("/exams/{id}/edited")
    // public String updateexam(@PathVariable(name = "id") Long id,
    // @RequestParam(name = "name") String name,
    // @RequestParam(name = "tag") String tag) throws NotFoundException {
    // Exam exam = examRepo.findExamById(id);
    // if (exam == null) {
    // throw new NotFoundException("Exam not found");
    // }
    // examRepo.editexamById(id, name, tag);
    // return "redirect:/exams/show";
    // }

    // @GetMapping("/exams/{id}/delete")
    // public String deleteexam(@PathVariable(name = "id") Long id) throws
    // NotFoundException {
    // Exam exam = examRepo.findExamById(id);
    // if (exam == null) {
    // throw new NotFoundException("Exam not found");
    // }
    // examRepo.deleteExamById(id);
    // return "redirect:/exams/show";
    // }

}
