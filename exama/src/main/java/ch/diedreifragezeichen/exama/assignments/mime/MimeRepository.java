package ch.diedreifragezeichen.exama.assignments.mime;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MimeRepository extends JpaRepository<Mime, Long> {
    public Mime findMimeById(Long id);

    public void deleteById(Long id);

    // public List<Mime> findAllByDuedateBetween(LocalDate Monday, LocalDate Sunday);

    public List<Mime> findAllByEditDateBetween(LocalDate Monday, LocalDate Sunday);

}
