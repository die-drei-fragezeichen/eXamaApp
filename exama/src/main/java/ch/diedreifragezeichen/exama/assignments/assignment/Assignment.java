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
        long daysToGo = ChronoUnit.DAYS.between(date, this.dueDate);
        if (daysToGo < 0) {
            return -1;
        }
        return (int) daysToGo;
    }

    public int getAvailableDaysTotal() {
        LocalDate startDate;
        if (this.startDate == null) {
            startDate = this.editDate;
        } else {
            startDate = this.startDate;
        }
        return (int) ChronoUnit.DAYS.between(startDate, this.dueDate);
    }

    public double getWorkloadValue(LocalDate date) {
        // not yet started
        if (this.startDate.isAfter(date)) {
            return 0;
        }
        // TODO: store this constant in the Database to be changable
        // specifice the number of workload hours for 100%
        double hundredPercent = 3.5;
        double hundredPercentMinutes = hundredPercent * 60;

        double workloadMinutes = this.getWorkloadMinutesOnDayX(this.getRealStartDate(), date, this.dueDate);

        return workloadMinutes / hundredPercentMinutes;
    }

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

    public double getWorkloadMinutesOnDayX(LocalDate startDate, LocalDate dayX, LocalDate dueDate) {
        if (startDate.isAfter(dayX)) {
            return 0;
        }
        double m;
        int diffDays = (int) ChronoUnit.DAYS.between(startDate, dueDate);
        int dayNumberInProcess = (int) ChronoUnit.DAYS.between(startDate, dayX);

        switch (this.workloadDistribution.getName()) {
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