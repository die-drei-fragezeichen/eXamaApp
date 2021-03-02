package ch.diedreifragezeichen.exama.TESTKLASSEN.exams;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.DynamicUpdate;

@Entity
@DynamicUpdate
public class Exam {
    // The @Id and @GeneratedValue annotations map the field id to the primary key
    // column of the table.
    @Id
    @Column(name = "id", unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "exam_name", unique = true, nullable = false, length = 20)
    private String name;

    @Temporal(TemporalType.DATE)
    private Date examDate;

    @Column(name = "counting_factor", nullable = false)
    private double countingFactor;

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

    public Date getExamDate() {
        return examDate;
    }

    public void setExamDate(Date examDate) {
        this.examDate = examDate;
    }

    public double getCountingFactor() {
        return countingFactor;
    }

    public void setCountingFactor(double countingFactor) {
        this.countingFactor = countingFactor;
    }

    
}