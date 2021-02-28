/* package ch.diedreifragezeichen.exama.courses;

import org.springframework.beans.factory.annotation.Autowired;

//with the @Service annotation, Spring framework will create an instance of 
//this class as a managed bean in the application context.
//@Service("subjectService")
public class CourseService {

    // With the @Autowired annotation Spring Data JPA will automatically inject a
    // currently "fake" but useable SubjectRepository instance into this
    // SubjectService class
    @Autowired
    @SuppressWarnings("unused")
    private CourseRepository courseRepo;

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
 */