package ch.diedreifragezeichen.exama.semesters;

import java.time.LocalDate;

import javax.persistence.*;

import org.hibernate.annotations.DynamicUpdate;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@DynamicUpdate
@Table(name = "holidays")
public class Holiday {
    /**
     * Fields
     */
    @Id
    @Column(unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 20)
    private String name;

    @Column(nullable = false)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate startDate;

    @Column(nullable = false)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate endDate;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "semester")
    private Semester semester;

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


    public LocalDate getStartDate() {
        return startDate;
    }


    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }


    public LocalDate getEndDate() {
        return endDate;
    }


    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }


    public Semester getSemester() {
        return semester;
    }


    public void setSemester(Semester semester) {
        this.semester = semester;
    }


    @Override
    public String toString() {
        return name + " (Beginn: " + startDate + ", Ende: " + endDate + ")";
    }


    

}