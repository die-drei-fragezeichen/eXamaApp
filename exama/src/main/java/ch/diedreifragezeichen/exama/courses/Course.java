package ch.diedreifragezeichen.exama.courses;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;

import ch.diedreifragezeichen.exama.userAdministration.User;

@Entity
@Table(name = "courses")
public class Course {

    @Id
    @Column(name = "course_id", unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "course_name", unique = true, nullable = false, length = 5)
    private String name;

    @Column(name = "course_enabled", nullable = false, length = 1)
    private boolean isEnabled;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "map_courses_students", joinColumns = @JoinColumn(name ="course_id"), inverseJoinColumns = @JoinColumn(name = "user_id"))
    private Set<User> courseStudents = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isEnabled() {
        return this.isEnabled;
    }

    public void setEnabled(boolean isEnabled) {
        this.isEnabled = isEnabled;
    }

    public Set<User> getCourseStudents() {
        return courseStudents;
    }

    public void setCourseStudents(Set<User> courseStudents) {
        this.courseStudents = courseStudents;
    }
}