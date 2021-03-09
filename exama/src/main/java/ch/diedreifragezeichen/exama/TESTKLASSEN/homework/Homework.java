/* package ch.diedreifragezeichen.exama.TESTKLASSEN.homework;

import java.time.LocalDate;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicUpdate;
import org.springframework.format.annotation.DateTimeFormat;

import ch.diedreifragezeichen.exama.assignments.Assignment;
import ch.diedreifragezeichen.exama.assignments.availablePrepTimes.AvailablePrepTime;
import ch.diedreifragezeichen.exama.assignments.workload.Workload;
import ch.diedreifragezeichen.exama.courses.Course;
import ch.diedreifragezeichen.exama.subjects.Subject;
import ch.diedreifragezeichen.exama.userAdministration.User;

@Entity
@Table(name = "homework")
@DynamicUpdate
public class Homework extends Assignment{
    @Id
    @Column(name = "homework_id", unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "homework_name", nullable = false, length = 20)
    private String name;
    @Column(name = "homework_startdate", nullable = false)
    // man muss eintragen, wann die HA aufgegeben wird
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate startDate;
    
    @Column(name = "homework_editdate", nullable = false)
    // nicht unique, man kann so mehrere HA auf einmal eintragen
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate editDate=startDate;


    @Column(name = "homework_duedate", nullable = false)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate dueDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "availablepreptime_id", nullable = true)
    private AvailablePrepTime availablePrepTime; // ManyToOne (Many assignments can have same PrepTime) -> At
                                                 // AvailablePrepTime it will be OneToMany (One Preptime can have many
                                                 // Assignments)
                                                 //Nullable, weil man die Spalte eigentlich nciht bruacht
                                                 //ManytoOne: meherer HA können dieselbe PrepTime haben
    @Column(name = "homework_description", nullable = true, length = 3000)
    private String description;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workload_id", nullable = false)
    private Workload workload; // OneToOne (One Assignment has one Workload an vice versa)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subject_id", nullable = false)
    private Subject subject;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User creator;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "map_homework_courses", joinColumns = @JoinColumn(name = "homework_id"), inverseJoinColumns = @JoinColumn(name = "course_id"))
    private Set<Course> courses;
} */