package ch.diedreifragezeichen.exama.subject;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

// This interface is powerful. it replaces writing boilerplate code for generic DataAccessObjects
// The Jpa Repository defines common persistence operations (including CRUD) 
//and the implementation will be generated at runtime by Spring Data JPA.

//The type parameter <Subject, Long> specifies: domain model class is Subject and the type of the primary key is Long.
//The CrudRepository interface defines common operations like save(), findAll(), findById(), delete(), count(), 
//you don't need to code any implementations!At runtime, Spring Data JPA takes care all the details.

public interface SubjectRepository extends JpaRepository<Subject, Long> {

    // declare query method
    // By default, the query definition uses JPQL.
    // Then we create REST APIs(GET, POST, PUT, DELETE)

    @Query("SELECT s FROM Subject s WHERE s.id = ?1")
    public Subject getSubjectByID(Long id);

    @Query("SELECT s FROM Subject s WHERE s.name = ?1")
    public Subject getSubjectByName(String name);

    @Query("SELECT s FROM Subject s WHERE s.tag = ?1")
    public Subject getSubjectByTag(String tag);
}

// Note that in the SubjectRepository interface, we can declare findByXXX()
// methods
// (XXX is the name of a field in the domain model class), and Spring Data JPA
// will generate the appropriate code: