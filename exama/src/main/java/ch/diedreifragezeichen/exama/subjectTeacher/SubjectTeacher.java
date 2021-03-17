package ch.diedreifragezeichen.exama.subjectTeacher;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;

import org.hibernate.annotations.DynamicUpdate;

import ch.diedreifragezeichen.exama.courses.Course;
import ch.diedreifragezeichen.exama.subjects.Subject;
import ch.diedreifragezeichen.exama.userAdministration.User;

@Entity
@DynamicUpdate
@Table(name = "subjects_teachers", uniqueConstraints = @UniqueConstraint(columnNames = {"subject_id", "teacher_id"}))
public class SubjectTeacher {
    /**
     * Fields
     */
    @Id
    @Column(unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Subject subject;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private User teacher;

    /**
     * ManyToMany mappings
     */
    @ManyToMany(cascade = CascadeType.PERSIST, mappedBy = "subjectTeachers")
    private Set<Course> courses = new HashSet<>();

    /**
     * Methods
     */
    @Override
    public boolean equals(Object obj) {
        SubjectTeacher castObj = (SubjectTeacher) obj;
        return this.subject.getId()==castObj.subject.getId() && this.teacher.getId()==castObj.teacher.getId();
    }
    
    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Subject getSubject() {
        return this.subject;
    }

    public void setSubjectId(Subject subject) {
        this.subject = subject;
    }

    public User getTeacherId() {
        return this.teacher;
    }

    public void setTeacherId(User teacher) {
        this.teacher = teacher;
    }
}