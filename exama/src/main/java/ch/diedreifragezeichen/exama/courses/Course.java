package ch.diedreifragezeichen.exama.courses;

import java.util.*;
import java.util.stream.Collectors;

import javax.persistence.*;

import org.hibernate.annotations.DynamicUpdate;

import ch.diedreifragezeichen.exama.assignments.exams.Exam;
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

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    /** Get CoreCourse call on Course */
    public CoreCourse getCoreCourse() {
        Set<User> studentSet = this.getUsers();
        if (studentSet == null) {
            return null;
        }
        List<User> studentList = studentSet.stream().filter(u -> Objects.nonNull(u.getCoreCourse()))
                .collect(Collectors.toList());
        if (studentList.size() == 0) {
            return null;
        }
        CoreCourse coreCourse = studentList.get(0).getCoreCourse();
        for (int i = 0; i < studentList.size() - 1; i++) {
            System.out.println(i);
            System.out.println(studentList.size());
            System.out.println(studentList.get(i).getCoreCourse().getName() + " / "
                    + studentList.get(i + 1).getCoreCourse().getName());
            if (!studentList.get(i).getCoreCourse().equals(studentList.get(i).getCoreCourse())) {
                return null;
            }
        }
        return coreCourse;
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
        List<CoreCourse> coreCourses = new ArrayList<CoreCourse>();
        for (User s : studentList) {
            coreCourses.add(s.getCoreCourse());
        }
        coreCourses = coreCourses.stream().distinct().collect(Collectors.toList());
        return coreCourses;
    }
}