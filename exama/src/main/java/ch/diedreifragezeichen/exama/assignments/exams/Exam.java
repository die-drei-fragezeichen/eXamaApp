package ch.diedreifragezeichen.exama.assignments.exams;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

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
     * Methods
     */
    @Override
    public double getWorkloadMinutesOnDayX(LocalDate startDate, LocalDate dayX, LocalDate dueDate) {
        if (startDate.isAfter(dayX)) {
            return 0;
        }
        if (dayX.minusDays(1).isAfter(dueDate)) {
            return 0;
        }

        int dayNumberInProcess = (int) ChronoUnit.DAYS.between(startDate, dayX);
        //if(dayNumberInProcess < 0 || dayNumberInProcess > this.getAvailablePrepTime().getDays()


        double workloadMinutes = 0;
        if(this.getWorkloadMinutesTotal() == 0){
            workloadMinutes = this.getExamType().getTimeValue()*60.0;
        } else{
            workloadMinutes = this.getWorkloadMinutesTotal();
        }


        double m;
        int diffDays = (int) ChronoUnit.DAYS.between(startDate, dueDate);
        switch (this.getWorkloadDistribution().getName()) {
        case "LINEAR":
            /* m = 2 * workloadMinutes / Math.pow(diffDays, 2);
            return m * dayNumberInProcess; */
            // for testing everything constant
            return workloadMinutes / diffDays;

        case "CONSTANT":
            return workloadMinutes / diffDays;

        case "EXPONENTIAL":
            double faktor = 1.1; // 10% more per day (faktor a)
            // function f(x)=a^x+b -> Integral from 0 to diffDays t is
            // a^t/ln(a)+b*t-1/ln(a)
            // Integral from 0 to diffDays must be workloadMinutesTotal w -> solve ->
            // b=-(a^t-ln(a)*w-1)/(ln(a)*t)
            // double workloadDayOne = -(Math.pow(faktor, diffDays) - Math.log(faktor) * workloadMinutes - 1)
            //         / (Math.log(faktor) * diffDays);
            // return Math.pow(faktor, dayNumberInProcess) + workloadDayOne;
            // for testing everything constant
            return workloadMinutes / diffDays;

        default: // LINEAR
            // m = 2 * workloadMinutes / Math.pow(diffDays, 2);
            // return m * dayNumberInProcess;
            // for testing everything constant
            return workloadMinutes / diffDays;
        }
    }


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
