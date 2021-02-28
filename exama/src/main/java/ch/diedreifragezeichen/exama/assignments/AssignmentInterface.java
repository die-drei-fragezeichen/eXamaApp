package ch.diedreifragezeichen.exama.assignments;

import java.util.Date;

public interface AssignmentInterface {

    /**
     * @param date should be the day for which the workload value will be returned
     * @return percentage of total allowed workload of the Assignment instance on a
     *         specific date
     */
    public double getWorkloadValue(Date date);

    /**
     * @param date should be the day from which the days until duedate will be
     *             returned
     * @return days to go until the due date
     */
    public int getAvailableDaysToGo(Date date);

    /**
     * @return the total availabledays to work on the assignment
     */
    public int getAvailableDaysTotal();

    /**
     * @return the "real" start date to include in the calculations
     */
    public Date getRealStartDate();
}
