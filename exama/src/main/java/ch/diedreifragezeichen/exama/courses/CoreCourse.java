package ch.diedreifragezeichen.exama.courses;

import java.util.*;
import java.util.stream.Collectors;

import javax.persistence.*;

import org.hibernate.annotations.DynamicUpdate;

import ch.diedreifragezeichen.exama.users.User;

@Entity
@DynamicUpdate
@Table(name = "coreCourses")
public class CoreCourse {
//    private static CourseRepository courseRepo;

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

    public List<Course> getCourses() {
        List<Course> allCourses = this.students.stream().map(u -> u.getCourses()).flatMap(Set::stream).distinct()
                .collect(Collectors.toList());
        return allCourses;// .stream().filter(c -> Objects.nonNull(c.getCoreCourse())).filter(c ->
                          // c.getCoreCourse().getId() == this.getId()).collect(Collectors.toList());
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

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((classTeacher == null) ? 0 : classTeacher.hashCode());
        result = prime * result + (enabled ? 1231 : 1237);
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((students == null) ? 0 : students.hashCode());
        return result;
    }

    // @Override
    // public boolean equals(Object obj) {
    //     if (this == obj)
    //         return true;
    //     if (obj == null)
    //         return false;
    //     if (getClass() != obj.getClass())
    //         return false;
    //     CoreCourse other = (CoreCourse) obj;
    //     if (classTeacher == null) {
    //         if (other.classTeacher != null)
    //             return false;
    //     } else if (!classTeacher.equals(other.classTeacher))
    //         return false;
    //     if (enabled != other.enabled)
    //         return false;
    //     if (id == null) {
    //         if (other.id != null)
    //             return false;
    //     } else if (!id.equals(other.id))
    //         return false;
    //     if (name == null) {
    //         if (other.name != null)
    //             return false;
    //     } else if (!name.equals(other.name))
    //         return false;
    //     if (students == null) {
    //         if (other.students != null)
    //             return false;
    //     } else if (!students.equals(other.students))
    //         return false;
    //     return true;
    // }

    
}