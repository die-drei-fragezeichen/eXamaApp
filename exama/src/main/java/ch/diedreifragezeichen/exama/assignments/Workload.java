package ch.diedreifragezeichen.exama.assignments;

import java.util.Date;
import java.util.concurrent.TimeUnit;

public class Workload implements WorkloadInterface {
    private long id;
    private double workloadMinutesTotal;
    private WorkloadDistribution distribution;

    public Workload(double workloadMinutesTotal, WorkloadDistribution distribution) {
        this.workloadMinutesTotal = workloadMinutesTotal;
        this.setDistribution(distribution);
    }

    public Workload(double workloadMinutesTotal) {
        this.workloadMinutesTotal = workloadMinutesTotal;
        this.setDistribution(distribution);
    }

    @Override
    public double getWorkloadMinutesTotal() {
        return this.workloadMinutesTotal;
    }

    public void setWorkloadMinutesTotal(double workloadMinutesTotal) {
        this.workloadMinutesTotal = workloadMinutesTotal;
    }

    public WorkloadDistribution getDistribution() {
        return this.distribution;
    }

    public void setDistribution(WorkloadDistribution distribution) {
        this.distribution = distribution;
    }

    @Override
    public double getWorkloadMinutesOnDayX(Date startDate, Date dayX, Date dueDate) {
        if (startDate.after(dayX)) {
            return 0;
        }
        double m;
        int diffDays = (int) TimeUnit.DAYS.convert(Math.abs(dueDate.getTime() - startDate.getTime()),
                TimeUnit.MILLISECONDS);
        int dayNumberInProcess = (int) TimeUnit.DAYS.convert(Math.abs(dueDate.getTime() - startDate.getTime()),
                TimeUnit.MILLISECONDS);

        switch (this.distribution.getName()) {
            case "LINEAR":
                m = 2 * workloadMinutesTotal / Math.pow(diffDays, 2);
                return m * dayNumberInProcess;

            case "CONSTANT":
                return workloadMinutesTotal / diffDays;

            case "EXPONENTIAL":
                double faktor = 1.1; // 10% more per day (faktor a)
                // function f(x)=a^x+b -> Integral from 0 to diffDays t is
                // a^t/ln(a)+b*t-1/ln(a)
                // Integral from 0 to diffDays must be workloadMinutesTotal w -> solve ->
                // b=-(a^t-ln(a)*w-1)/(ln(a)*t)
                double workloadDayOne = -(Math.pow(faktor, diffDays) - Math.log(faktor) * workloadMinutesTotal - 1)
                        / (Math.log(faktor) * diffDays);
                return Math.pow(faktor, dayNumberInProcess) + workloadDayOne;

            default: // LINEAR
                m = 2 * workloadMinutesTotal / Math.pow(diffDays, 2);
                return m * dayNumberInProcess;
        }
    }
}