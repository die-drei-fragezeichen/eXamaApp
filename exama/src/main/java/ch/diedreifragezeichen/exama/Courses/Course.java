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

    //Dies ist der KlassenName zum Beispiel "L4a" oder "2018E"
    @Column(name = "course_name", unique = true, nullable = false, length = 5)
    private String courseName;

    @Column(name = "is_enabled", nullable = false, length = 1)
    private boolean isEnabled;

	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "course_teachers",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "course_id")
    )
    private Set<User> courseTeachers = new HashSet<>();


    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "course_students",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "course_id")
    )
    private Set<User> courseStudents = new HashSet<>();


    // @Lob
	// @Column(name = "workloads", columnDefinition="BLOB", nullable = true)
	// //@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	// //@JoinTable(name = "courses", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "course_id"))
	// private Set<Workload> workloads = new HashSet<>();


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseTag(String courseName) {
        this.courseName = courseName;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean isEnabled) {
        this.isEnabled = isEnabled;
    }

	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}

	public Set<User> getCourseTeachers() {
		return courseTeachers;
	}

	public void setCourseTeachers(Set<User> courseTeachers) {
		this.courseTeachers = courseTeachers;
	}

	public Set<User> getCourseStudents() {
		return courseStudents;
	}

	public void setCourseStudents(Set<User> courseStudents) {
		this.courseStudents = courseStudents;
	}

	}
