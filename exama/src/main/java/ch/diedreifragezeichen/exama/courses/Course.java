package ch.diedreifragezeichen.exama.courses;

import java.util.*;
import java.util.stream.Collectors;

import javax.persistence.*;

import org.hibernate.annotations.DynamicUpdate;

import ch.diedreifragezeichen.exama.assignments.exams.Exam;
import ch.diedreifragezeichen.exama.assignments.homeworks.Homework;
import ch.diedreifragezeichen.exama.subjects.Subject;
import ch.diedreifragezeichen.exama.users.User;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = true)
    private Subject subject;

    // unnecessary field -> all data allready saved -> look at getCoreCourse /
    // getAllCoreCourses
    /*
     * @ManyToOne(fetch = FetchType.LAZY)
     * 
     * @JoinColumn(nullable = true) private CoreCourse coreCourse;
     */

    /**
     * OneToMany mappings
     */
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "course")
    private List<Exam> exams;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "course")
    private List<Homework> homeworks;

    /**
     * ManyToMany Mapping with user
     */
    @ManyToMany(cascade = CascadeType.PERSIST, mappedBy = "courses")
    private Set<User> users = new HashSet<>();

    /**
     * Methods
     */
    @Override
    public String toString() {
        return this.name;
    }

    public List<User> getUsersList() {
        List<User> userList = new ArrayList<>(this.getUsers());
        return userList;
    }

    /**
     * Get CoreCourse call on Course returns the CoreCourse of a specific Course if
     * there is only one CoreCourse present returns null if there are no students in
     * the course or if there are different CoreCourses present (returns the same
     * than before with the field CoreCourse, which was set to null on a course with
     * students form different courses)
     */
    public CoreCourse getCoreCourse() {
        Set<User> studentSet = this.getUsers();
        // return null, if there are no users in the course
        if (studentSet == null) {
            return null;
        }
        List<User> studentList = studentSet.stream().filter(u -> Objects.nonNull(u.getCoreCourse()))
                .collect(Collectors.toList());
        // return null, if there are no students in the course (a teacher doesnt have a
        // coreCourse)
        if (studentList.size() == 0) {
            return null;
        }
        int numCoreCourses = studentList.stream().map(User::getCoreCourse).distinct().collect(Collectors.toList())
                .size();
        // return null, if there are students from more than one coreCourses present in
        // the course
        if (numCoreCourses != 1) {
            return null;
        }
        return studentList.get(0).getCoreCourse();
    }

    /** Get a list of all CoreCourse present in a specific Course */
    public List<CoreCourse> getAllCoreCourses() {
        Set<User> studentSet = this.getUsers();
        if (studentSet == null) {
            return null;
        }
        List<User> studentList = studentSet.stream().filter(u -> Objects.nonNull(u.getCoreCourse()))
                .collect(Collectors.toList());
        if (studentList.size() == 0) {
            return null;
        }
        // return all different CoureCourses present in the Studentlist as a List.
        return studentList.stream().map(User::getCoreCourse).distinct().collect(Collectors.toList());
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

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public List<Exam> getExams() {
        return exams;
    }

    public void setExams(List<Exam> exams) {
        this.exams = exams;
    }

    public List<Homework> getHomeworks() {
        return this.homeworks;
    }

    public void setHomeworks(List<Homework> homeworks) {
        this.homeworks = homeworks;
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }
}