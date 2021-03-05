package ch.diedreifragezeichen.exama.TESTKLASSEN.datums;

import java.time.LocalDate;
import javax.persistence.*;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
public class Datum {
    @Id
    @Column(name = "selcted_Date", nullable = true)
    @DateTimeFormat(iso= DateTimeFormat.ISO.DATE)
    private LocalDate selectedDate;

    public LocalDate getSelectedDate() {
        return selectedDate;
    }

    public void setSelectedDate(LocalDate selectedDate) {
        this.selectedDate = selectedDate;
    }

    
}
