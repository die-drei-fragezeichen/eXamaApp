package ch.diedreifragezeichen.exama.assignments.exams;

import javax.persistence.*;

import org.hibernate.annotations.DynamicUpdate;

import ch.diedreifragezeichen.exama.assignments.Assignment;
import ch.diedreifragezeichen.exama.assignments.examTypes.ExamType;

@Entity
@DynamicUpdate
@Table(name = "exams")
public class Exam extends Assignment {
    /**
     * Fields
     */
    @Column(nullable = false)
    private double countingFactor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private ExamType examType;

    /**
     * Methods
     */
    public ExamType getExamType() {
        return examType;
    }

    public void setType(ExamType type) {
        this.examType = type;
    }

    public double getCountingFactor() {
        return countingFactor;
    }

    public void setCountingFactor(double countingFactor) {
        this.countingFactor = countingFactor;
    }
}
