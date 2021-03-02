package ch.diedreifragezeichen.exama.TESTKLASSEN.exams;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ExamRepository extends JpaRepository<Exam, Long>{
    
// this repository makes / organises SQL data searches

    public Exam findExamById(Long id);

    public List<Exam> findAllByExamDate(Date examDate);
    
    public long deleteByName(String examName);

    public List<Exam> findAllByExamDateBetween(
        Date Monday,
        Date Sunday);

    
}
