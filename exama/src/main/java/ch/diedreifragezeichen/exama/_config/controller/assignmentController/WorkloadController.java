package ch.diedreifragezeichen.exama._config.controller.assignmentController;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import ch.diedreifragezeichen.exama.assignments.workload.Workload;
import ch.diedreifragezeichen.exama.assignments.workload.WorkloadRepository;


@Controller
public class WorkloadController {
    
    @Autowired
    private WorkloadRepository workloadRepo;
    
    @GetMapping("/workloads/show")
    public String showWorkloads(Model model){
        List<Workload> listWorkloads = workloadRepo.findAll();
        model.addAttribute("liste", listWorkloads);
        return "adminTemplates/workloadsShow";
    }

    @GetMapping("/workloads/create")
    public ModelAndView newWorkload(){
        Workload workload = new Workload();
        ModelAndView mav = new ModelAndView("adminTemplates/workloadCreate");
        mav.addObject("newWorkload", workload);
        return mav;
    }

    @PostMapping("/workloads/created")
    public String processSaving(Workload workload){
        workloadRepo.save(workload);
        return "redirect:/workloads/show";
    }
    
}
