package ch.diedreifragezeichen.exama.assignments.exams;

import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hibernate.annotations.DynamicUpdate;

import ch.diedreifragezeichen.exama.assignments.Assignment;
import ch.diedreifragezeichen.exama.assignments.availablePrepTimes.AvailablePrepTime;
import ch.diedreifragezeichen.exama.assignments.examTypes.ExamType;
import ch.diedreifragezeichen.exama.assignments.workload.Workload;
import ch.diedreifragezeichen.exama.courses.Course;
import ch.diedreifragezeichen.exama.subjects.Subject;

// @Entity
// @DynamicUpdate
@SuppressWarnings("unused")
public class PRUEFUNGEN extends Assignment {
    // The @Id and @GeneratedValue annotations map the field id to the primary key
    // column of the table.
    // @Id
    // @Column(name = "id", unique = true, nullable = false)
    // @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private long creator;

    private Set<Course> courses;

    private Subject subject;

    private Date editDate;
    private Date startDate;
    private Date dueDate;
    private AvailablePrepTime availableTime;
    private String description;
    private Workload workload;
    private ExamType type;

    public ExamType getType() {
        return type;
    }

    public void setType(ExamType type) {
        this.type = type;
    }

}
