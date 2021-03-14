package ch.diedreifragezeichen.exama.courses;

import java.util.List;

import javax.persistence.*;

import org.hibernate.annotations.DynamicUpdate;

import ch.diedreifragezeichen.exama.assignments.exams.Exam;

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
}