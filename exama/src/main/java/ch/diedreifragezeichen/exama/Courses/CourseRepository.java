/* package ch.diedreifragezeichen.exama.courses;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

// This interface is powerful. it replaces writing boilerplate code for generic DataAccessObjects
// The Jpa Repository defines common persistence operations (including CRUD) 
//and the implementation will be generated at runtime by Spring Data JPA.

//The type parameter <Subject, Long> specifies: domain model class is Subject and the type of the primary key is Long.
//The CrudRepository interface defines common operations like save(), findAll(), findById(), delete(), count(), 
//you don't need to code any implementations!At runtime, Spring Data JPA takes care all the details.

public interface CourseRepository extends JpaRepository<Course, Long> {

    // declare query method
    // By default, the query definition uses JPQL.
    // Then we create REST APIs(GET, POST, PUT, DELETE)
    // Careful: "Course" here refers not to the table but to a Course object u
    @Query("SELECT c FROM Course c WHERE c.id = ?1")
    public Course getCourseByID(Long id);
}
 */