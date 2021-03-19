package ch.diedreifragezeichen.exama.semesters;

import org.springframework.data.jpa.repository.JpaRepository;

public interface HolidayRepository extends JpaRepository<Holiday, Long> {
    public Holiday findHolidayById(Long id);
}
