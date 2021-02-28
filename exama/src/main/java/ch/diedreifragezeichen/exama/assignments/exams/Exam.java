package ch.diedreifragezeichen.exama.assignments.exams;

import java.util.Date;
import java.util.Set;

import ch.diedreifragezeichen.exama.assignments.Assignment;
import ch.diedreifragezeichen.exama.assignments.availablePrepTimes.AvailablePrepTime;
import ch.diedreifragezeichen.exama.assignments.Workload;
import ch.diedreifragezeichen.exama.assignments.examTypes.ExamType;
import ch.diedreifragezeichen.exama.courses.Course;

@SuppressWarnings("unused")
public class Exam extends Assignment {

    private long id;
    private long creator;
    private Set<Course> courses;
    private int subject;
    private Date editDate;
    private Date startDate;
    private Date dueDate;
    private AvailablePrepTime availableTime;
    private String description;
    private Workload workload;
    private ExamType type;

    public ExamType getType() {
        return type;
    }

    public void setType(ExamType type) {
        this.type = type;
    }

}
