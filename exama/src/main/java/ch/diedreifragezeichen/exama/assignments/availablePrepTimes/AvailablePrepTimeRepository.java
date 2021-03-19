package ch.diedreifragezeichen.exama.assignments.availablePrepTimes;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AvailablePrepTimeRepository extends JpaRepository<AvailablePrepTime, Long> {
    public AvailablePrepTime findPrepTimeById(Long id);
    public AvailablePrepTime findPrepTimeByName(String name);
    public AvailablePrepTime findPrepTimeByDays(int days);
    public void deletePrepTimeById(Long id);
}