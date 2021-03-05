/* package ch.diedreifragezeichen.exama.assignments.exams;

import java.time.LocalDate;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicUpdate;
import org.springframework.format.annotation.DateTimeFormat;

import ch.diedreifragezeichen.exama.assignments.Assignment;
import ch.diedreifragezeichen.exama.assignments.availablePrepTimes.AvailablePrepTime;
import ch.diedreifragezeichen.exama.assignments.examTypes.ExamType;
import ch.diedreifragezeichen.exama.assignments.workload.Workload;
import ch.diedreifragezeichen.exama.courses.Course;
import ch.diedreifragezeichen.exama.subjects.Subject;
import ch.diedreifragezeichen.exama.userAdministration.User;

@Entity
@DynamicUpdate
@Table(name = "exams")
public class ExamNew extends Assignment {
    @Id
    @Column(name = "exam_id", unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "exam_name", nullable = false, length = 20)
    private String name;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User creator;
    
    private Set<Course> courses;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subject_id", nullable = false)
    private Subject subject;

    @Column(name = "exam_editDate", nullable = false)
    @DateTimeFormat(iso=DateTimeFormat.ISO.DATE)
    private LocalDate editDate;
    
    @Column(name = "exam_startDate", nullable = true)
    @DateTimeFormat(iso=DateTimeFormat.ISO.DATE)
    private LocalDate startDate;

    @Column(name = "exam_dueDate", nullable = false)
    @DateTimeFormat(iso=DateTimeFormat.ISO.DATE)
    private LocalDate dueDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "availablepreptime_id")
    private AvailablePrepTime availableTime;

    @Column(name = "exam_description", nullable = true)
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workload_id", nullable = false)
    private Workload workload;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "examtype_id", nullable = false)
    private ExamType type;


    public ExamType getExamType() {
        return type;
    }

    public void setType(ExamType type) {
        this.type = type;
    }
}
 */