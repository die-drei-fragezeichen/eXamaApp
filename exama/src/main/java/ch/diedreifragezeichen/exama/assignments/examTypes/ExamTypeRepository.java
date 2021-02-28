package ch.diedreifragezeichen.exama.assignments.examTypes;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;

public interface ExamTypeRepository extends JpaRepository<ExamType, Long>{

    @Query("SELECT et FROM ExamType et WHERE et.id = ?1")
    public ExamType getExamTypeByID(Long id);

    @Query("SELECT et FROM ExamType et WHERE et.tag = ?1")
    public ExamType getExamTypeByTag(String tag);

    @Query("SELECT et FROM ExamType et WHERE et.name = ?1")
    public ExamType getExamTypeByName(String name);

    @Transactional
    @Modifying
    @Query("DELETE ExamType et WHERE et.id = ?1")
    public void deleteTypeById(long id);

    @Transactional
    @Modifying
    @Query("UPDATE ExamType et SET et.tag=?2, et.name=?3, et.timeValue=?4 WHERE et.id = ?1")
    public void editExamTypeById(long id, String tag, String name, double timeValue);

}