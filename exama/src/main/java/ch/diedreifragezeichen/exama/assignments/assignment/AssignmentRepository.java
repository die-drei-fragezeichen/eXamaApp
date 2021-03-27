package ch.diedreifragezeichen.exama.assignments.assignment;


import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import ch.diedreifragezeichen.exama.courses.*;

public interface AssignmentRepository extends JpaRepository<Assignment, Long> {
    public List<Assignment> findAll();

    public void deleteById(Long id);
    
    public List<Assignment> findAllByCoreCourse(CoreCourse course);

    public List<Assignment> findAllByDueDateBetween(LocalDate Monday, LocalDate Sunday);
}
