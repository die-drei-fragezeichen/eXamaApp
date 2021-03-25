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
       int daysToGo = this.getAvailableDaysToGo(dayX);
        if(daysToGo==-1 || (daysToGo>this.getAvailablePrepTime().getDays() && this.getAvailablePrepTime().getDays() != -1)){
            return 0;
        }
        
        //dayNumberInProcess is the n-th day of working on the assignment
        //if the set preptime (days) is shorter than the days between dayX and dueDate,
        //dayNumbeInProcess = -1
        int dayNumberInProcess = (int) ChronoUnit.DAYS.between(startDate, dayX);

        //if workloadminutes are not set by the user, take the timevalue of the examtype as workloadminutes
        double workloadMinutes = 0;
        if(this.getWorkloadMinutesTotal() == 0){
            workloadMinutes = this.getExamType().getTimeValue()*60.0;
        } else{
            workloadMinutes = this.getWorkloadMinutesTotal();
        }

        double m;
        switch (this.getWorkloadDistribution().getName()) {
        case "linear":
            m = 2 * workloadMinutes / Math.pow(this.getAvailableDaysTotal(), 2);
            return m * (dayNumberInProcess+0.5);

        case "konstant":
            return workloadMinutes / this.getAvailableDaysTotal();

        case "exponentiell":
            double faktor = 1.4; // 40% more per day (faktor b)
            // function f(x)=a*b^x -> Integral from 0 to diffDays t is (a*(b^t-1))/ln(b)
            // Integral from 0 to diffDays must be workloadMinutesTotal
            // solve -> a=(ln(b)*w)/(b^t-1)
            double workloadDayOne = (Math.log(faktor)*workloadMinutes)/(Math.pow(faktor, this.getAvailableDaysTotal()) - 1);
            return workloadDayOne*Math.pow(faktor, dayNumberInProcess+0.5);

        default: // konstant
            return workloadMinutes / this.getAvailableDaysTotal();
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
