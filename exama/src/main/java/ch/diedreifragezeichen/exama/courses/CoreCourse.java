package ch.diedreifragezeichen.exama.courses;

import java.util.*;

import javax.persistence.*;

import org.hibernate.annotations.DynamicUpdate;

import ch.diedreifragezeichen.exama.users.User;

@Entity
@DynamicUpdate
@Table(name = "coreCourses")
public class CoreCourse {
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = true)
    private User classTeacher;

    /**
     * OneToMany mappings
     */
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "coreCourse")
    private List<User> students;

    

    /**
     * Methods
     */
    @Override
    public String toString() {
        return this.name;
    }    

    /**
     * Getters and Setters only
     */
    public Long getId() {
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

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public User getClassTeacher() {
        return classTeacher;
    }

    public void setClassTeacher(User classTeacher) {
        this.classTeacher = classTeacher;
    }

    public List<User> getStudents() {
        return students;
    }

    public void setStudents(List<User> students) {
        this.students = students;
    }
}