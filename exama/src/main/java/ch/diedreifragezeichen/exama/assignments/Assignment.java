package ch.diedreifragezeichen.exama.assignments;

import java.util.Date;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import ch.diedreifragezeichen.exama.assignments.availablePrepTimes.AvailablePrepTime;
import ch.diedreifragezeichen.exama.assignments.workload.Workload;
import ch.diedreifragezeichen.exama.courses.Course;
import ch.diedreifragezeichen.exama.subjects.Subject;
import ch.diedreifragezeichen.exama.userAdministration.User;
//this is an abstract class
//no objects can be instatiated
public abstract class Assignment implements AssignmentInterface {

    private long id;
    private String name;
    private User creator;
    private Set<Course> courses;
    private Subject subject;
    private Date editDate;
    private Date startDate;
    private Date dueDate;
    private AvailablePrepTime availableTime; // ManyToOne (Many assignments can have same PrepTime) ->  At AvailablePrepTime it will be OneToMany (One Preptime can have many Assignments)
    private String description;
    private Workload workload; // OneToOne (One Assignment has one Workload an vice versa)

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    public Set<Course> getCourses() {
        return courses;
    }

    public void setCourses(Set<Course> courses) {
        this.courses = courses;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
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

    public AvailablePrepTime getAvailableTime() {
        return availableTime;
    }

    public void setAvailableTime(AvailablePrepTime availableTime) {
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
        // TODO: store this constant in the Database to be changable
        // specifice the number of workload hours for 100%
        double hundredPercent = 3.5;
        double hundredPercentMinutes = hundredPercent * 60;

        double workloadMinutes = this.workload.getWorkloadMinutesOnDayX(this.getRealStartDate(), date, this.dueDate);

        return workloadMinutes / hundredPercentMinutes;
    }

    @Override
    public Date getRealStartDate() {
        int diffDays = (int) TimeUnit.DAYS.convert(Math.abs(this.dueDate.getTime() - this.startDate.getTime()),
                TimeUnit.MILLISECONDS);
        if (availableTime.getDays() == -1 || availableTime.getName().equals("All Time")
                || diffDays < availableTime.getDays()) {
            return this.startDate;
        } else {
            return new Date(this.dueDate.getTime() - availableTime.getDays() * 24 * 60 * 60 * 1000);
        }
    }
}
