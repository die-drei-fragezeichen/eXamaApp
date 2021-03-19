package ch.diedreifragezeichen.exama.assignments.examTypes;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ExamTypeRepository extends JpaRepository<ExamType, Long> {
    public ExamType findExamTypeById(long id);
}