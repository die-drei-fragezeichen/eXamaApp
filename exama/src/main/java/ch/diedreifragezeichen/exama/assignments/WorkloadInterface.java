package ch.diedreifragezeichen.exama.assignments;

import java.util.Date;

public interface WorkloadInterface {

    /**
     * @return the total workload in minutes
     */
    public double getWorkloadMinutesTotal();

    /**
     * @param startDate is the start date included in the calculation
     * @param dayX      is the date on which the workload time will be returned
     * @param dueDate   is the end date included in the calculation
     * @return the number of workload minutes on dayX
     */
    public double getWorkloadMinutesOnDayX(Date startDate, Date dayX, Date dueDate);
}
