package ch.diedreifragezeichen.exama.operator;

import java.time.LocalDate;

import javax.persistence.*;

import org.springframework.format.annotation.DateTimeFormat;

import ch.diedreifragezeichen.exama.semesters.Semester;

@Entity
public class Operator {
    @Id
    @Column(unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate selectedDate;

    @ManyToOne(fetch = FetchType.LAZY)
    private Semester selectedSemester;

    /*
     * Getters and Setters only
     */

    public LocalDate getSelectedDate() {
        return selectedDate;
    }

    public void setSelectedDate(LocalDate selectedDate) {
        this.selectedDate = selectedDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Semester getSelectedSemester() {
        return selectedSemester;
    }

    public void setSelectedSemester(Semester selectedSemester) {
        this.selectedSemester = selectedSemester;
    }

}
