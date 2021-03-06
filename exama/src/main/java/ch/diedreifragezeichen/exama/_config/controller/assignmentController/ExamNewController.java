package ch.diedreifragezeichen.exama._config.controller.assignmentController;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import ch.diedreifragezeichen.exama.assignments.availablePrepTimes.AvailablePrepTime;
import ch.diedreifragezeichen.exama.assignments.availablePrepTimes.AvailablePrepTimeRepository;
import ch.diedreifragezeichen.exama.assignments.examTypes.ExamType;
import ch.diedreifragezeichen.exama.assignments.examTypes.ExamTypeRepository;
import ch.diedreifragezeichen.exama.assignments.exams.ExamNew;
import ch.diedreifragezeichen.exama.assignments.exams.ExamNewRepository;
import ch.diedreifragezeichen.exama.assignments.workload.Workload;
import ch.diedreifragezeichen.exama.assignments.workload.WorkloadDistribution;
import ch.diedreifragezeichen.exama.assignments.workload.WorkloadDistributionRepository;
import ch.diedreifragezeichen.exama.assignments.workload.WorkloadRepository;
import ch.diedreifragezeichen.exama.courses.Course;
import ch.diedreifragezeichen.exama.courses.CourseRepository;
import ch.diedreifragezeichen.exama.subjects.Subject;
import ch.diedreifragezeichen.exama.subjects.SubjectRepository;


@Controller
public class ExamNewController {
    @Autowired
    private ExamNewRepository examRepo;
    @Autowired
    private WorkloadRepository workloadRepo;
    @Autowired
    private WorkloadDistributionRepository distributionRepo;
    @Autowired
    private ExamTypeRepository examtypeRepo;
    @Autowired
    private CourseRepository courseRepo;
    @Autowired
    private AvailablePrepTimeRepository preptimeRepo;
    @Autowired
    private SubjectRepository subjectRepo;

    
    @GetMapping("/examsNew/show")
    public String showExams(Model model){
        List<ExamNew> listExams = examRepo.findAll();
        model.addAttribute("listExams", listExams);
        return "adminTemplates/examsNewShow";
    }

    @GetMapping("/examsNew/create")
    public ModelAndView newExam(){
        ModelAndView mav = new ModelAndView("adminTemplates/examNewCreate");
        ExamNew exam = new ExamNew();
        mav.addObject("newExam", exam);
        Workload workload = new Workload();
        mav.addObject("newWorkload", workload);
        List<WorkloadDistribution> listDist = distributionRepo.findAll();
        mav.addObject("allDist", listDist);
        List<Course> listCourses = courseRepo.findAll();
        mav.addObject("allCourses", listCourses);
        List<ExamType> listTypes = examtypeRepo.findAll();
        mav.addObject("allTypes", listTypes);
        List<Subject> listSubjects = subjectRepo.findAll();
        mav.addObject("allSubjects", listSubjects);
        List<AvailablePrepTime> listPrepTime = preptimeRepo.findAll();
        mav.addObject("allPreptimes", listPrepTime);
        return mav;
    }

    @PostMapping("/examsNew/created")
    public String processSaving(Workload workload, ExamNew exam){
        workloadRepo.save(workload);
        exam.setWorkload(workload);
        exam.setEditDate(LocalDate.now());
        examRepo.save(exam);
        return "redirect:/examsNew/show";
    }
    
}
