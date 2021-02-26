package ch.diedreifragezeichen.exama.assignments;

import java.util.List;
import java.util.concurrent.TimeUnit;

import ch.diedreifragezeichen.exama.assignments.Workload.PrepareTime;
import ch.diedreifragezeichen.exama.assignments.Workload.WorkloadDistribution;

import java.util.ArrayList;
import java.util.Date;

@SuppressWarnings("unused")
public class Assignment {
    private long id;
    private long creator;
    private int course;
    private int subject;
    private Date editDate;
    private Date startDate;
    private Date dueDate;
    private Workload workload;

    public Assignment(long id, long user, int course, int subject, Date editDate, Date startDate, Date dueDate,
            Workload workload) {
        this.id = id;
        this.creator = user;
        this.course = course;
        this.subject = subject;
        this.editDate = editDate;
        this.startDate = startDate;
        this.dueDate = dueDate;
        this.workload = workload;
    }

    public long getId() {
        return id;
    }

    public long getUser() {
        return creator;
    }

    public void setUser(long user) {
        this.creator = user;
    }

    public int getCourse() {
        return course;
    }

    public void setCourse(int course) {
        this.course = course;
    }

    public int getSubject() {
        return subject;
    }

    public void setSubject(int subject) {
        this.subject = subject;
    }

    public Date getEditDate() {
        return editDate;
    }

    public void setEditDate(Date editDate) {
        this.editDate = editDate;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public Workload getWorkload() {
        return workload;
    }

    public void setWorkload(Workload workload) {
        this.workload = workload;
    }

    public long getNumberOfDays() {
        long diff = Math.abs(this.dueDate.getTime() - this.startDate.getTime());
        return TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
    }

    public double getWorkloadValue(Date date) {
        // not yet started
        if (this.startDate.after(date)) {
            return 0;
        }

        double workingTime = this.workload.getWorkingTime();
        WorkloadDistribution dist = this.workload.getDistribution();

        double prepareTime = this.workload.getPrepareTime();
        if (prepareTime == -1 || prepareTime > this.getNumberOfDays()) {
            prepareTime = this.getNumberOfDays();
        }

        double m;
        int dayNumberInProcess = (int) TimeUnit.DAYS.convert(Math.abs(date.getTime() - this.startDate.getTime()),
                TimeUnit.MILLISECONDS);

        switch (dist) {
            case CONSTANT:
                return workingTime / prepareTime;

            case EXPONENTIAL:
                double faktor = 1.1; // 10% more per day (faktor a)
                // function f(x)=a^x+b -> Integral from 0 to prepareTime t is
                // a^t/ln(a)+b*t-1/ln(a)
                // Integral from 0 to prepareTime must be workingTime w -> solve -> b=
                // -(a^t-ln(a)*w-1)/(ln(a)*t)
                double workloadDayOne = -(Math.pow(faktor, prepareTime) - Math.log(faktor) * workingTime - 1)
                        / (Math.log(faktor) * prepareTime);
                return Math.pow(faktor, dayNumberInProcess) + workloadDayOne;

            case LINEAR:
                m = 2 * workingTime / Math.pow(prepareTime, 2);
                return m * dayNumberInProcess;

            default: // LINEAR
                m = 2 * workingTime / Math.pow(prepareTime, 2);
                return m * dayNumberInProcess;
        }
    }
}
