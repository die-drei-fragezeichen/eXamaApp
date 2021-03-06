package ch.diedreifragezeichen.exama.assignments.exams;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import ch.diedreifragezeichen.exama.courses.Course;

public interface ExamRepository extends JpaRepository<Exam, Long> {
    public Exam findExamById(Long id);

    public void deleteById(Long id);

    public List<Exam> findAllByCourse(Course course);

    public List<Exam> findAllByDueDateBetween(LocalDate Monday, LocalDate Sunday);
}