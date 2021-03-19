package ch.diedreifragezeichen.exama.userAdministration;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.*;

import org.hibernate.annotations.DynamicUpdate;
import org.springframework.format.annotation.DateTimeFormat;

import ch.diedreifragezeichen.exama.assignments.exams.Exam;
import ch.diedreifragezeichen.exama.courses.Course;
import ch.diedreifragezeichen.exama.courses.CoreCourse;

@Entity
@DynamicUpdate
@Table(name = "users")
public class User {
	/**
	 * Fields
	 */
	@Id
	@Column(unique = true, nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(unique = true, nullable = false, length = 45)
	private String email;

	@Column(nullable = false, length = 255)
	private String password;

	@Column(nullable = false, length = 20)
	private String firstName;

	@Column(nullable = false, length = 20)
	private String lastName;

	@Column(nullable = true, length = 3, unique = true)
	private String abbreviation;

	@Column(nullable = false)
	private boolean enabled;

	@Column(nullable = false)
	private boolean loggedIn;

	@Column(nullable = true)
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	private LocalDate lastLogin;

	@Column(nullable = false)
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	private LocalDate createdOn;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = true)
	private CoreCourse coreCourse;

	@ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
	@JoinTable(joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "course_id"))
	private Set<Course> courses = new HashSet<>();

	@ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
	@JoinTable(joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
	private Set<Role> roles = new HashSet<>();

	/**
	 * OneToMany mappings
	 */
	@OneToMany(mappedBy = "creator")
	private List<Exam> exams;

	@OneToMany(mappedBy = "classTeacher")
	private List<CoreCourse> classTeacherCourses;

	/**
	 * Methods
	 */
	@Override
	public String toString() {
		String toString = "";
		if (this.abbreviation != null) {
			toString += "("+this.abbreviation+") ";
		}
		toString += this.firstName + " " + this.lastName;
		return toString;
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

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFirstName() {
		return this.firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return this.lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getAbbreviation() {
		return abbreviation;
	}

	public void setAbbreviation(String abbreviation) {
		this.abbreviation = abbreviation;
	}

	public boolean isEnabled() {
		return this.enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public Set<Role> getRoles() {
		return this.roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	public boolean isLoggedIn() {
		return this.loggedIn;
	}

	public void setLoggedIn(boolean loggedIn) {
		this.loggedIn = loggedIn;
	}

	public LocalDate getLastLogin() {
		return this.lastLogin;
	}

	public void setLastLogin(LocalDate lastLogin) {
		this.lastLogin = lastLogin;
	}

	public LocalDate getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(LocalDate createdOn) {
		this.createdOn = createdOn;
	}

	public CoreCourse getCoreCourse() {
		return coreCourse;
	}

	public void setCoreCourse(CoreCourse coreCourse) {
		this.coreCourse = coreCourse;
	}

	public Set<Course> getCourses() {
		return courses;
	}

	public void setCourses(Set<Course> courses) {
		this.courses = courses;
	}

	public List<Exam> getExams() {
		return exams;
	}

	public void setExams(List<Exam> exams) {
		this.exams = exams;
	}

	public List<CoreCourse> getClassTeacherCourses() {
		return classTeacherCourses;
	}

	public void setClassTeacherCourses(List<CoreCourse> classTeacherCourses) {
		this.classTeacherCourses = classTeacherCourses;
	}
}