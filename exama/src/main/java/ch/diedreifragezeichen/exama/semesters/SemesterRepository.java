package ch.diedreifragezeichen.exama.semesters;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SemesterRepository extends JpaRepository<Semester, Long> {
    public Semester findSemesterById(Long id);
}