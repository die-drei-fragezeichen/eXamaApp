package ch.diedreifragezeichen.exama.assignments.availablePrepTimes;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;

public interface AvailablePrepTimeRepository extends JpaRepository<AvailablePrepTime, Long>{

    @Query("SELECT pt FROM AvailablePrepTime pt WHERE pt.id = ?1")
    public AvailablePrepTime getPrepTimeByID(Long id);

    @Query("SELECT pt FROM AvailablePrepTime pt WHERE pt.name = ?1")
    public AvailablePrepTime getPrepTimeByName(String name);

    @Query("SELECT pt FROM AvailablePrepTime pt WHERE pt.days = ?1")
    public AvailablePrepTime getPrepTimeByDays(int days);

    @Transactional
    @Modifying
    @Query("DELETE AvailablePrepTime pt WHERE pt.id = ?1")
    public void deletePrepTimeById(long id);

    @Transactional
    @Modifying
    @Query("UPDATE AvailablePrepTime pt SET pt.name=?2, pt.days=?3 WHERE pt.id = ?1")
    public void editPrepTimeById(long id, String name, int days);

}