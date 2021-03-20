package ch.diedreifragezeichen.exama.subjects;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SubjectRepository extends JpaRepository<Subject, Long> {
    public Subject findSubjectById(Long id);
    public Subject findSubjectByName(String name);
    public Subject findSubjectByTag(String tag);
}