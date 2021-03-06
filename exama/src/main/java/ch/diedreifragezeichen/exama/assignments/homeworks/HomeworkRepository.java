package ch.diedreifragezeichen.exama.assignments.homeworks;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import ch.diedreifragezeichen.exama.courses.Course;

public interface HomeworkRepository extends JpaRepository<Homework, Long> {
    public Homework findHomeworkById(Long id);

    public List<Homework> findAllByCourse(Course course);

    public List<Homework> findAllByDueDateBetween(LocalDate Monday, LocalDate Sunday);
}