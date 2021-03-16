package ch.diedreifragezeichen.exama.courses;

import java.util.*;

import javax.persistence.*;

import org.hibernate.annotations.DynamicUpdate;

import ch.diedreifragezeichen.exama.assignments.exams.Exam;
import ch.diedreifragezeichen.exama.subjects.Subject;

@Entity
@DynamicUpdate
@Table(name = "courses")
public class Course {
    /**
     * Fields
     */
    @Id
    @Column(unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 20)
    private String name;

    @Column(nullable = false)
    private boolean enabled;

    @ManyToMany(cascade = CascadeType.MERGE)
    @JoinTable(name = "map_courses_subjects", joinColumns = { @JoinColumn(name = "courseid")   }, inverseJoinColumns = {@JoinColumn(name = "subjectid") })
    private Set<Subject> subjects = new HashSet<Subject>(0);

    /**
     * OneToMany mappings
     */
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "course")
    private List<Exam> exams;

    /*
     * @OneToMany(cascade = CascadeType.ALL, mappedBy = "course") private
     * List<Homework> homeworks;
     */

    /**
     * Methods
     */
    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public List<Exam> getExams() {
        return exams;
    }

    public void setExams(List<Exam> exams) {
        this.exams = exams;
    }

    @Override
    public String toString() {
        return this.name;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Set<Subject> getSubjects() {
        return subjects;
    }

    public void setSubjects(Set<Subject> subjects) {
        this.subjects = subjects;
    }
}