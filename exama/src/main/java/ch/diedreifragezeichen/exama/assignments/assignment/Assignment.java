package ch.diedreifragezeichen.exama.assignments.assignment;

import java.time.LocalDate;

import javax.persistence.*;

import org.springframework.format.annotation.DateTimeFormat;

import java.time.temporal.ChronoUnit;

import ch.diedreifragezeichen.exama.assignments.availablePrepTimes.AvailablePrepTime;
import ch.diedreifragezeichen.exama.assignments.workloadDistributions.WorkloadDistribution;
import ch.diedreifragezeichen.exama.courses.Course;
import ch.diedreifragezeichen.exama.users.User;

// can not be abstract because of the ManyToOne-Mapping
//no objects should be instatiated
@MappedSuperclass
public class Assignment {
    /**
     * Fields
     */
    @Id
    @Column(unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 20)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private User creator;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Course course;

    @Column(nullable = false)
    private LocalDate editDate;

    @Column(nullable = true)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate startDate;

    @Column(nullable = false)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate dueDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private AvailablePrepTime availablePrepTime;

    @Column(nullable = true, length = 4095)
    private String description;

    @Column(nullable = true)
    private double workloadMinutesTotal;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private WorkloadDistribution workloadDistribution;

    /**
     * Methods
     */
    public int getAvailableDaysToGo(LocalDate date) {
        int daysToGo = (int) ChronoUnit.DAYS.between(date, this.dueDate) - 1;
        if (daysToGo < 0) {
            return -1;
        }
        return daysToGo;
    }

    public int getAvailableDaysTotal() {
        return (int) ChronoUnit.DAYS.between(this.getRealStartDate(), this.dueDate);
    }

    public double getWorkloadValue(LocalDate date) {
        // not yet started
        if (this.getRealStartDate().isAfter(date)) {
            return 0;
        } else {
            // 3.5 hours is the max of workload besides classes in school
            double hundredPercent = 3.5;
            double hundredPercentMinutes = hundredPercent * 60;
            double workloadMinutes = this.getWorkloadMinutesOnDayX(this.getRealStartDate(), date, this.dueDate);
            return workloadMinutes / hundredPercentMinutes;
        }
    }

    public LocalDate getRealStartDate() {
        LocalDate realStartDate;
        if (this.availablePrepTime.getDays() == -1 || this.availablePrepTime.getName().equals("ganze Zeit")) {
            if (this.startDate == null) {
                realStartDate = this.editDate;
            } else {
                realStartDate = this.startDate;
            }
        } else {
            realStartDate = this.dueDate.minusDays(this.availablePrepTime.getDays());
        }
        return realStartDate;
    }

    public double getWorkloadMinutesOnDayX(LocalDate startDate, LocalDate dayX, LocalDate dueDate) {
        int daysToGo = this.getAvailableDaysToGo(dayX);
        if (daysToGo == -1
                || (daysToGo > this.getAvailablePrepTime().getDays() && this.getAvailablePrepTime().getDays() != -1)) {
            return 0;
        }

        // dayNumberInProcess is the n-th day of working on the assignment
        // if the set preptime (days) is shorter than the days between dayX and dueDate,
        // dayNumbeInProcess = -1
        int dayNumberInProcess = (int) ChronoUnit.DAYS.between(startDate, dayX);

        double workloadMinutes = this.getWorkloadMinutesTotal();

        double m;
        switch (this.getWorkloadDistribution().getName()) {
        case "linear":
            m = 2 * workloadMinutes / Math.pow(this.getAvailableDaysTotal(), 2);
            return m * (dayNumberInProcess + 0.5);

        case "konstant":
            return workloadMinutes / this.getAvailableDaysTotal();

        case "exponentiell":
            double faktor = 1.4; // 40% more per day (faktor b)
            // function f(x)=a*b^x -> Integral from 0 to diffDays t is (a*(b^t-1))/ln(b)
            // Integral from 0 to diffDays must be workloadMinutesTotal
            // solve -> a=(ln(b)*w)/(b^t-1)
            double workloadDayOne = (Math.log(faktor) * workloadMinutes)
                    / (Math.pow(faktor, this.getAvailableDaysTotal()) - 1);
            return workloadDayOne * Math.pow(faktor, dayNumberInProcess + 0.5);

        default: // konstant
            return workloadMinutes / this.getAvailableDaysTotal();
        }
    }

    /**
     * Getters and Setters only
     */
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
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

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public LocalDate getEditDate() {
        return editDate;
    }

    public void setEditDate(LocalDate editDate) {
        this.editDate = editDate;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public AvailablePrepTime getAvailablePrepTime() {
        return availablePrepTime;
    }

    public void setAvailablePrepTime(AvailablePrepTime availablePrepTime) {
        this.availablePrepTime = availablePrepTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getWorkloadMinutesTotal() {
        return workloadMinutesTotal;
    }

    public void setWorkloadMinutesTotal(double workloadMinutesTotal) {
        this.workloadMinutesTotal = workloadMinutesTotal;
    }

    public WorkloadDistribution getWorkloadDistribution() {
        return workloadDistribution;
    }

    public void setWorkloadDistribution(WorkloadDistribution workloadDistribution) {
        this.workloadDistribution = workloadDistribution;
    }
}