package ch.diedreifragezeichen.exama.courses;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;

import ch.diedreifragezeichen.exama.userAdministration.User;
import ch.diedreifragezeichen.exama.subjects.Subject;

@Entity
@Table(name = "courses")
public class Course {

    @Id
    @Column(name = "course_id", unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Dies ist der KlassenName zum Beispiel "L4a" oder "2018E"
    @Column(name = "course_name", unique = true, nullable = false, length = 5)
    private String name;

    @Column(name = "is_enabled", nullable = false, length = 1)
    private boolean isEnabled;

    // @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    // @JoinTable(name = "courses_subjects_teachers", joinColumns = {
    //     @JoinColumn(table="courses", name = "course_id", referencedColumnName = "id"),
    //     @JoinColumn(table ="subjects", name = "subject_id", referencedColumnName = "id")},
    //     inverseJoinColumns = @JoinColumn(table="users", name = "user_id", referencedColumnName = "id"))
    // private Set<User> courseTeachers = new HashSet<>();

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinTable(name = "courses_subjects_teachers", joinColumns = @JoinColumn(name = "course_id"), inverseJoinColumns = @JoinColumn(name = "user_id"))
	private Set<User> courseTeachers = new HashSet<>();

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinTable(name = "courses_students", joinColumns = @JoinColumn(name = "course_id"), inverseJoinColumns = @JoinColumn(name = "user_id"))
	private Set<User> courseStudents = new HashSet<>();

    // @Lob
    // @Column(name = "workloads", columnDefinition="BLOB", nullable = true)
    // //@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    // //@JoinTable(name = "courses", joinColumns = @JoinColumn(name = "user_id"),
    // inverseJoinColumns = @JoinColumn(name = "course_id"))
    // private Set<Workload> workloads = new HashSet<>();

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

    public Set<User> getCourseTeachers() {
        return this.courseTeachers;
    }

    public void setCourseTeachers(Set<User> courseTeachers) {
        this.courseTeachers = courseTeachers;
    }

    public Set<User> getCourseStudents() {
        return this.courseStudents;
    }

    public void setCourseStudents(Set<User> courseStudents) {
        this.courseStudents = courseStudents;
    }

}
