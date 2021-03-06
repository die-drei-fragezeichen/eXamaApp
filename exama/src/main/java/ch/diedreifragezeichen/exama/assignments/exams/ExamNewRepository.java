package ch.diedreifragezeichen.exama.assignments.exams;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ExamNewRepository extends JpaRepository<ExamNew, Long> {
    public ExamNew findExamNewById(Long id);
    public void deleteById(Long id);
    public List<ExamNew> findAllByDueDateBetween(LocalDate Monday,LocalDate Sunday);
}
