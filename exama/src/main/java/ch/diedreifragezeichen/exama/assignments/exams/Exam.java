package ch.diedreifragezeichen.exama.assignments.exams;

import javax.persistence.*;

import org.hibernate.annotations.DynamicUpdate;

import ch.diedreifragezeichen.exama.assignments.assignment.Assignment;
import ch.diedreifragezeichen.exama.assignments.examTypes.ExamType;
import ch.diedreifragezeichen.exama.semesters.Semester;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Semester semester;

    /**
     * Getters and Setters only
     */

    public double getCountingFactor() {
        return countingFactor;
    }

    public void setCountingFactor(double countingFactor) {
        this.countingFactor = countingFactor;
    }

    public ExamType getExamType() {
        return examType;
    }

    public void setExamType(ExamType examType) {
        this.examType = examType;
    }

    public Semester getSemester() {
        return semester;
    }

    public void setSemester(Semester semester) {
        this.semester = semester;
    }
}
