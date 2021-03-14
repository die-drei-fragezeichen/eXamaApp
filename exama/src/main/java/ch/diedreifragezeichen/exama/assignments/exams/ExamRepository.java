package ch.diedreifragezeichen.exama.assignments.exams;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ExamRepository extends JpaRepository<Exam, Long> {
    public Exam findExamNewById(Long id);

    public void deleteById(Long id);

    public List<Exam> findAllByDueDateBetween(LocalDate Monday, LocalDate Sunday);
}
