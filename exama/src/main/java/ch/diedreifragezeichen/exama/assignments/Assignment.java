package ch.diedreifragezeichen.exama.assignments;

import java.util.Date;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import ch.diedreifragezeichen.exama.courses.Course;

public abstract class Assignment implements AssignmentInterface {

    public enum availablePrepTime {
        ALLTIME, SEVENDAYS, FOURTEENDAYS
    }

    private long id;
    private long creator;
    private Set<Course> courses;
    private int subject;
    private Date editDate;
    private Date startDate;
    private Date dueDate;
    private availablePrepTime availableTime;
    private String description;
    private Workload workload;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getCreator() {
        return creator;
    }

    public void setCreator(long creator) {
        this.creator = creator;
    }

    public Set<Course> getCourses() {
        return courses;
    }

    public void setCourses(Set<Course> courses) {
        this.courses = courses;
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

    public availablePrepTime getAvailableTime() {
        return availableTime;
    }

    public void setAvailableTime(availablePrepTime availableTime) {
        this.availableTime = availableTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Workload getWorkload() {
        return workload;
    }

    public void setWorkload(Workload workload) {
        this.workload = workload;
    }

    @Override
    public int getAvailableDaysToGo(Date date) {
        long diff = Math.abs(this.dueDate.getTime() - date.getTime());
        return (int) TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
    }

    @Override
    public int getAvailableDaysTotal() {
        long diff = Math.abs(this.dueDate.getTime() - this.startDate.getTime());
        return (int) TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
    }

    @Override
    public double getWorkloadValue(Date date) {
        // not yet started
        if (this.startDate.after(date)) {
            return 0;
        }
        //TODO: store this constant in the Database to be changable
        //specifice the number of workload hours for 100%
        double hundredPercent = 3.5;
        double hundredPercentMinutes = hundredPercent*60;

        double workloadMinutes = this.workload.getWorkloadMinutesOnDayX(this.getRealStartDate(), date, this.dueDate);

        return workloadMinutes / hundredPercentMinutes;
    }

    @Override
    public Date getRealStartDate() {
        int diffDays = (int) TimeUnit.DAYS.convert(Math.abs(this.dueDate.getTime() - this.startDate.getTime()), TimeUnit.MILLISECONDS);
        switch(availableTime){
            case ALLTIME:
                return this.startDate;
            case SEVENDAYS:
                if(diffDays<7){
                    return this.startDate;
                }
                return new Date(this.dueDate.getTime()-7*24*60*60*1000);
            case FOURTEENDAYS:
                if(diffDays<14){
                    return this.startDate;
                }
                return new Date(this.dueDate.getTime()-14*24*60*60*1000);
            default:
                return this.startDate;
        }
    }
}
