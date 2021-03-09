/* package ch.diedreifragezeichen.exama.assignments.homework;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface HomeworkRepository extends JpaRepository<Homework, Long> {

    // this repository makes / organises SQL data searches

    public Homework findHomeworkById(Long id);

    // public List<Exam> findAllByExamDate(Date examDate);

    public long deleteByID(Long homeworkID);

    public List<Homework> findAllByDateBetween(LocalDate Monday, LocalDate Sunday);
} */