package ch.diedreifragezeichen.exama._config.controller.assignmentController;

import java.time.LocalDate;
import java.util.List;

//import javassist.NotFoundException;

import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import ch.diedreifragezeichen.exama.TESTKLASSEN.exams.ExamRepository;
import ch.diedreifragezeichen.exama.TESTKLASSEN.exams.ExamService;
import ch.diedreifragezeichen.exama.TESTKLASSEN.datums.Datum;
import ch.diedreifragezeichen.exama.TESTKLASSEN.datums.DatumRepository;
import ch.diedreifragezeichen.exama.TESTKLASSEN.exams.Exam;

@Controller
public class ExamController {
    @Autowired
    private ExamRepository examRepo;

    @Autowired
    private DatumRepository datumRepo;

    /**
     * The following methods handle the examBar
     */

    /**
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
    @GetMapping("/examBar/show")
    public String showExamBarForSavedDate(Model model) {
        Datum selectedDatum = datumRepo.findAll().get(0);
        datumRepo.deleteAll();
        LocalDate selectedDate = selectedDatum.getSelectedDate();
        ExamService helper = new ExamService();
        LocalDate Monday = helper.getFirstDayOfWeek(selectedDate);
        LocalDate Sunday = helper.getLastDayOfWeek(selectedDate);

        List<Exam> listExams = examRepo.findAllByDateBetween(Monday, Sunday);
        model.addAttribute("liste", listExams);
        return "studentTemplates/examBarShow";
    }

    /**
     * The following methods handle the examCreation
     */


    /**
     * user arriving at this path is enabled to create a new exam. When arriving at the path
     * a ModelAndView is created and the HTML link injected. New Exam "template"
     * is added to the ModelAndView and returned to the "HTML" to be "filled"
     */

    @GetMapping("/exams/create")
    public ModelAndView newExam() {
        Exam exam = new Exam();
        ModelAndView mav = new ModelAndView("adminTemplates/examsCreate");
        mav.addObject("exam", exam);
        // mav.addAttribute("standardDate", new LocalDate());

        return mav;
    }

    /**
     * When clicking the submit button, the User sends an Exam object.
     * The examRepo saves this object into the db
     * note that the link here is different than in the get method, but the user never really sees it
     * user is redirected to the "show" page immediately
     */

    @PostMapping("/exams/created")
    public String processSaving(Exam exam) {
        examRepo.save(exam);
        return "redirect:/exams/show";
    }


    /**
     * user arrives at exams/show and sees a list of all the exams in DB.
     */

    @GetMapping("/exams/show")
    public String showExams(Model model) {
        List<Exam> listWorkloads = examRepo.findAll();
        model.addAttribute("liste", listWorkloads);
        return "adminTemplates/examsShow";
    }

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
