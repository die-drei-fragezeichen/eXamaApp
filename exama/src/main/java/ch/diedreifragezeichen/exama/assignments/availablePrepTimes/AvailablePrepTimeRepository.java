package ch.diedreifragezeichen.exama.assignments.availablePrepTimes;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AvailablePrepTimeRepository extends JpaRepository<AvailablePrepTime, Long> {
    public AvailablePrepTime findAvailablePrepTimeById(Long id);
    public AvailablePrepTime findAvailablePrepTimeByName(String name);
    public AvailablePrepTime findAvailablePrepTimeByDays(int days);
}