package ch.diedreifragezeichen.exama.assignments.workload;

import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkloadDistributionRepository extends JpaRepository<WorkloadDistribution, Long> {

    public WorkloadDistribution findWorkloadDistributionById(Long id);
}
