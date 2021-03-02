package ch.diedreifragezeichen.exama.assignments.workload;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;

import ch.diedreifragezeichen.exama.exams.ExamRepository;
import javassist.NotFoundException;

public class WorkloadService {
    @Autowired
    private WorkloadRepository workloadRepo;

    @PersistenceContext
    EntityManager em;

    public Workload loadByID(Long id) throws NotFoundException {
        Workload workload = workloadRepo.findWorkloadById(id);
        if (workload == null) {
            throw new NotFoundException("Workload not found");
        }
        return workload;
    }
}
