// package ch.diedreifragezeichen.exama._tobetrashed;

// import java.time.LocalDate;
// import java.time.temporal.ChronoUnit;

// import javax.persistence.*;

// import org.hibernate.annotations.DynamicUpdate;

// @Entity
// @DynamicUpdate
// @Table(name = "workloads")
// public class Workload {
//     @Id
//     @Column(unique = true, nullable = false)
//     @GeneratedValue(strategy = GenerationType.IDENTITY)
//     private Long id;

//     @Column(nullable = false)
//     private double workloadMinutesTotal;

//     @ManyToOne(fetch = FetchType.LAZY)
//     @JoinColumn
//     private WorkloadDistribution distribution;

//     public Long getId() {
//         return this.id;
//     }

//     public double getWorkloadMinutesTotal() {
//         return this.workloadMinutesTotal;
//     }

//     public void setWorkloadMinutesTotal(double workloadMinutesTotal) {
//         this.workloadMinutesTotal = workloadMinutesTotal;
//     }

//     public WorkloadDistribution getDistribution() {
//         return this.distribution;
//     }

//     public void setDistribution(WorkloadDistribution distribution) {
//         this.distribution = distribution;
//     }

//     public double getWorkloadMinutesOnDayX(LocalDate startDate, LocalDate dayX, LocalDate dueDate) {
//         if (startDate.isAfter(dayX)) {
//             return 0;
//         }
//         double m;
//         int diffDays = (int) ChronoUnit.DAYS.between(startDate, dueDate);
//         int dayNumberInProcess = (int) ChronoUnit.DAYS.between(startDate, dayX);

//         switch (this.distribution.getName()) {
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
//         }
//     }
// } */