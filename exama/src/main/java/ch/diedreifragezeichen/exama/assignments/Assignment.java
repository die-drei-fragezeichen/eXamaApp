package ch.diedreifragezeichen.exama.assignments;

import java.time.LocalDate;
import java.util.Set;
import java.time.temporal.ChronoUnit;

import ch.diedreifragezeichen.exama.assignments.availablePrepTimes.AvailablePrepTime;
import ch.diedreifragezeichen.exama.assignments.workload.Workload;
import ch.diedreifragezeichen.exama.courses.Course;
import ch.diedreifragezeichen.exama.subjects.Subject;
import ch.diedreifragezeichen.exama.userAdministration.User;

//this is an abstract class
//no objects can be instatiated
public abstract class Assignment implements AssignmentInterface {

    private Long id;
    private String name;
    private User creator;
    private Set<Course> courses;
    private Subject subject;
    private LocalDate editDate;
    private LocalDate startDate;
    private LocalDate dueDate;
    private AvailablePrepTime availablePrepTime; // ManyToOne (Many assignments can have same PrepTime) -> At
                                                 // AvailablePrepTime it will be OneToMany (One Preptime can have many
                                                 // Assignments)
    private String description;
    private Workload workload; // OneToOne (One Assignment has one Workload an vice versa)

    public Long getId() {
        return this.id;
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
        return this.creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    public Set<Course> getCourses() {
        return this.courses;
    }

    public void setCourses(Set<Course> courses) {
        this.courses = courses;
    }

    public Subject getSubject() {
        return this.subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public LocalDate getEditDate() {
        return this.editDate;
    }

    public void setEditDate(LocalDate editDate) {
        this.editDate = editDate;
    }

    public LocalDate getStartDate() {
        return this.startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getDueDate() {
        return this.dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public AvailablePrepTime getAvailablePrepTime() {
        return this.availablePrepTime;
    }

    public void setAvailablePrepTime(AvailablePrepTime availablePrepTime) {
        this.availablePrepTime = availablePrepTime;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Workload getWorkload() {
        return this.workload;
    }

    public void setWorkload(Workload workload) {
        this.workload = workload;
    }

    @Override
    public int getAvailableDaysToGo(LocalDate date) {
        long daysToGo = ChronoUnit.DAYS.between(date, this.dueDate);
        if (daysToGo < 0) {
            return -1;
        }
        return (int) daysToGo;
    }

    @Override
    public int getAvailableDaysTotal() {
        LocalDate startDate;
        if (this.startDate == null) {
            startDate = this.editDate;
        } else {
            startDate = this.startDate;
        }
        return (int) ChronoUnit.DAYS.between(startDate, this.dueDate);
    }

    @Override
    public double getWorkloadValue(LocalDate date) {
        // not yet started
        if (this.startDate.isAfter(date)) {
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
    public LocalDate getRealStartDate() {
        LocalDate startDate;
        if (this.startDate == null) {
            startDate = this.editDate;
        } else {
            startDate = this.startDate;
        }
        int diffDays = (int) ChronoUnit.DAYS.between(startDate, this.dueDate);
        if (availablePrepTime.getDays() == -1 || availablePrepTime.getName().equals("All Time")
                || diffDays < availablePrepTime.getDays()) {
            return startDate;
        } else {
            startDate = this.dueDate.minusDays(this.availablePrepTime.getDays());
            return startDate;
        }
    }
}
