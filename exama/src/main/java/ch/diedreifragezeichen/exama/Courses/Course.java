package ch.diedreifragezeichen.exama.Courses;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;

import ch.diedreifragezeichen.exama.userAdministration.User;

@Entity
@Table(name = "courses")
public class Course {

    @Id
    @Column(name = "id", unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "course_name", unique = true, nullable = false, length = 20)
    private String courseName;

    @Column(name = "course_tag", unique = true, nullable = false, length = 4)
    private String courseTag;

    @Column(name = "is_enabled", nullable = false, length = 1)
    private boolean isEnabled;

    @Column(name = "is_locked", nullable = false, length = 1)
    private boolean isLocked;

    // When we want to create
    // multiple join columns we can use the @JoinColumns annotation: foreign keys
    // pointing to ... ???
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "users", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "courses"))
    private Set<User> teachers = new HashSet<>();

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "usesrs", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "courses"))
    private Set<User> students = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getCourseTag() {
        return courseTag;
    }

    public void setCourseTag(String courseTag) {
        this.courseTag = courseTag;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean isEnabled) {
        this.isEnabled = isEnabled;
    }

    public boolean isLocked() {
        return isLocked;
    }

    public void setLocked(boolean isLocked) {
        this.isLocked = isLocked;
    }

    public Set<User> getTeachers() {
        return teachers;
    }

    public void setTeachers(Set<User> teachers) {
        this.teachers = teachers;
    }

    public Set<User> getStudents() {
        return students;
    }

    public void setStudents(Set<User> students) {
        this.students = students;
    }

    
}
