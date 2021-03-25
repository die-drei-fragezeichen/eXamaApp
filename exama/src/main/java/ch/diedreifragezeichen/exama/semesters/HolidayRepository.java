package ch.diedreifragezeichen.exama.semesters;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface HolidayRepository extends JpaRepository<Holiday, Long> {
    public Holiday findHolidayById(Long id);

    public List<Holiday> findAllByStartDateBetween(LocalDate Monday, LocalDate Sunday);

}
