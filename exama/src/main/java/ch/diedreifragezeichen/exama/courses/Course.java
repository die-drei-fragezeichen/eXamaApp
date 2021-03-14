package ch.diedreifragezeichen.exama.courses;

import java.util.List;

import javax.persistence.*;

import org.hibernate.annotations.DynamicUpdate;

import ch.diedreifragezeichen.exama.assignments.Assignment;
import ch.diedreifragezeichen.exama.assignments.exams.Exam;

@Entity
@DynamicUpdate
@Table(name = "courses")
public class Course {
    
    @Id
    @Column(name="course_id", unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 20)
    private String name;

    //TODO: List<Assignment> not working because its not an entity. -> make list of superclass working!
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "course")
    private List<Exam> assignments;

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    @Override
    public String toString(){
        return this.name;
    }
    // public List<Assignment> getAssignments() {
    //     return assignments;
    // }

    // public void setAssignments(List<Assignment> assignments) {
    //     this.assignments = assignments;
    // }

    
}
