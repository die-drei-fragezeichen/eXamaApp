package ch.diedreifragezeichen.exama.assignments.mime;

import java.time.LocalDate;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
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
@Table(name = "mimes")
public class Mime extends Assignment {
    @Id
    @Column(name = "id", unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // @Column(name = "exam_name", nullable = false, length = 20)
    private String name;

    // @Column(name = "exam_editdate", nullable = false)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate editDate;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate startDate;

    public Long getId(){
        return id;

    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getEditDate() {
        return editDate;
    }

    public void setEditDate(LocalDate editDate) {
        this.editDate = editDate;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }



    // @Column(name = "exam_startdate", nullable = true)
    // @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    // private LocalDate startdate;



    // @Column(name = "exam_duedate", nullable = false)
    // @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    // private LocalDate dueDate;

    // @Column(name = "exam_description", nullable = true, length = 255)
    // private String description;

    // @Column(name = "exam_countingfactor", nullable = false)
    // private double countingFactor;

    // @ManyToOne(fetch = FetchType.LAZY)
    // @JoinColumn(name = "availablepreptime_id", nullable = false)
    // private AvailablePrepTime availablePrepTime;

    // @OneToOne(fetch = FetchType.LAZY)
    // @JoinColumn(name = "workload_id", nullable = false)
    // private Workload workload;

    // @ManyToOne(fetch = FetchType.LAZY)
    // @JoinColumn(name = "examtype_id", nullable = false)
    // private ExamType examType;

    // @ManyToOne(fetch = FetchType.LAZY)
    // @JoinColumn(name = "subject_id", nullable = false)
    // private Subject subject;

    // @ManyToOne(fetch = FetchType.LAZY)
    // @JoinColumn(name = "user_id", nullable = false)
    // private User creator;

    // @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    // @JoinTable(name = "map_exams_courses", joinColumns = @JoinColumn(name = "exam_id"), inverseJoinColumns = @JoinColumn(name = "course_id"))
    // private Set<Course> courses;

   
}
