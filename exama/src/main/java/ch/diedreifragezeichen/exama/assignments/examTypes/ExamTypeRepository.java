package ch.diedreifragezeichen.exama.assignments.examTypes;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ExamTypeRepository extends JpaRepository<ExamType, Long> {

    public ExamType findExamTypeById(long id);

    // @Query("SELECT et FROM ExamType et WHERE et.id = ?1")
    // public ExamType getExamTypeByID(Long id);

    // @Query("SELECT et FROM ExamType et WHERE et.name = ?1")
    // public ExamType getExamTypeByName(String name);

    // @Transactional
    // @Modifying
    // @Query("DELETE ExamType et WHERE et.id = ?1")
    // public void deleteExamTypeById(long id);

    // @Transactional
    // @Modifying
    // @Query("UPDATE ExamType et SET et.name=?2 WHERE et.id = ?1")
    // public void editExamTypeById(long id, String name);

}