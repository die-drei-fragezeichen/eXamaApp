package ch.diedreifragezeichen.exama.assignments.workload;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicUpdate;

import ch.diedreifragezeichen.exama.assignments.WorkloadInterface;

@Entity
@DynamicUpdate
@Table(name = "workloads")
public class Workload implements WorkloadInterface {
    @Id
    @Column(name = "workload_id", unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "workload_minutestotal", nullable = false)
    private double workloadMinutesTotal;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workloaddistribution_id")
    private WorkloadDistribution distribution;

    public Long getId(){
        return this.id;
    }

    @Override
    public double getWorkloadMinutesTotal() {
        return this.workloadMinutesTotal;
    }

    public void setWorkloadMinutesTotal(double workloadMinutesTotal) {
        this.workloadMinutesTotal = workloadMinutesTotal;
    }

    @Override
    public double getWorkloadMinutesOnDayX(Date startDate, Date dayX, Date dueDate) {
        // TODO Auto-generated method stub
        return 0;
    }

    public WorkloadDistribution getDistribution() {
         return this.distribution;
    }

    public void setDistribution(WorkloadDistribution distribution) {
        this.distribution = distribution;
    }

    // @Override
    // public double getWorkloadMinutesOnDayX(Date startDate, Date dayX, Date dueDate) {
    //     if (startDate.after(dayX)) {
    //         return 0;
    //     }
    //     double m;
    //     int diffDays = (int) TimeUnit.DAYS.convert(Math.abs(dueDate.getTime() - startDate.getTime()),
    //             TimeUnit.MILLISECONDS);
    //     int dayNumberInProcess = (int) TimeUnit.DAYS.convert(Math.abs(dueDate.getTime() - startDate.getTime()),
    //             TimeUnit.MILLISECONDS);

    //     switch (this.distribution.getName()) {
    //         case "LINEAR":
    //             m = 2 * workloadMinutesTotal / Math.pow(diffDays, 2);
    //             return m * dayNumberInProcess;

    //         case "CONSTANT":
    //             return workloadMinutesTotal / diffDays;

    //         case "EXPONENTIAL":
    //             double faktor = 1.1; // 10% more per day (faktor a)
    //             // function f(x)=a^x+b -> Integral from 0 to diffDays t is
    //             // a^t/ln(a)+b*t-1/ln(a)
    //             // Integral from 0 to diffDays must be workloadMinutesTotal w -> solve ->
    //             // b=-(a^t-ln(a)*w-1)/(ln(a)*t)
    //             double workloadDayOne = -(Math.pow(faktor, diffDays) - Math.log(faktor) * workloadMinutesTotal - 1)
    //                     / (Math.log(faktor) * diffDays);
    //             return Math.pow(faktor, dayNumberInProcess) + workloadDayOne;

    //         default: // LINEAR
    //             m = 2 * workloadMinutesTotal / Math.pow(diffDays, 2);
    //             return m * dayNumberInProcess;
    //     }
    // }
}