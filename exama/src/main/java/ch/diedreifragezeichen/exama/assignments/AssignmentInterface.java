package ch.diedreifragezeichen.exama.assignments;

import java.time.LocalDate;

public interface AssignmentInterface {

    /**
     * @param date should be the day for which the workload value will be returned
     * @return percentage of total allowed workload of the Assignment instance on a
     *         specific date
     */
    public double getWorkloadValue(LocalDate date);

    /**
     * @param date should be the day from which the days until duedate will be
     *             returned
     * @return days to go until the due date
     */
    public int getAvailableDaysToGo(LocalDate date);

    /**
     * @return the total availabledays to work on the assignment
     */
    public int getAvailableDaysTotal();

    /**
     * @return the "real" start date to include in the calculations
     */
    public LocalDate getRealStartDate();
}
