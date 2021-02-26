package ch.diedreifragezeichen.exama.subject;

import javax.persistence.*;

import org.springframework.beans.factory.annotation.Autowired;

import javassist.NotFoundException;

//with the @Service annotation, Spring framework will create an instance of 
//this class as a managed bean in the application context.
//@Service("subjectService")
public class SubjectService {

    // With the @Autowired annotation Spring Data JPA will automatically inject a
    // currently "fake" but useable SubjectRepository instance into this
    // SubjectService class
    @Autowired
    private SubjectRepository subjectRepo;

    @PersistenceContext
    EntityManager em;

    public Subject loadByID(Long id) throws NotFoundException {
        Subject subject = subjectRepo.getSubjectByID(id);
        if (subject == null) {
            throw new NotFoundException("Subject not found");
        }
        return subject;
    }

    public Subject loadByName(String name) throws NotFoundException {
        Subject subject = subjectRepo.getSubjectByName(name);
        if (subject == null) {
            throw new NotFoundException("Subject not found");
        }
        return subject;
    }

    public Subject loadByTag(String tag) throws NotFoundException {
        Subject subject = subjectRepo.getSubjectByTag(tag);
        if (subject == null) {
            throw new NotFoundException("Subject not found");
        }
        return subject;
    }
}

// For further inspiration:
// Find a subject by Id
// Optional<Subject> result = subjectRepo.findById(1L);
// result.ifPresent(subject -> System.out.println(subject.getSubjectName()));

// // Find subject by tag
// Subject wanted = subjectRepo.findSubjectByName("English");
// System.out.println(wanted.getSubjectName());

// // Find subject by tag
// List<Subject> allSubjects = subjectRepo.findSubjectByTag("ENG");
// allSubjects.forEach(subject -> System.out.println(subject.getId()));

// // List all subjects
// Iterable<Subject> iterator = subjectRepo.findAll();
// iterator.forEach(subject -> System.out.println(subject));

// // Count number of subjects
// long countSubjects = subjectRepo.count();
// System.out.println("Number of customers: " + countSubjects);