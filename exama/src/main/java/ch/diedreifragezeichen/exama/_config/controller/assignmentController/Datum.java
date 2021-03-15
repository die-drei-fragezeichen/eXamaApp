package ch.diedreifragezeichen.exama._config.controller.assignmentController;

import java.time.LocalDate;

import javax.persistence.*;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
public class Datum {
    @Id
    @Column(nullable = true)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate selectedDate;

    public LocalDate getSelectedDate() {
        return selectedDate;
    }

    public void setSelectedDate(LocalDate selectedDate) {
        this.selectedDate = selectedDate;
    }

}
