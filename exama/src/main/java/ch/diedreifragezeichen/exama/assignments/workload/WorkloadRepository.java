package ch.diedreifragezeichen.exama.assignments.workload;

import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkloadRepository extends JpaRepository<Workload, Long>{
    
    public Workload findWorkloadById(Long id);
}