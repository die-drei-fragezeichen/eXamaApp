package ch.diedreifragezeichen.exama.assignments;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;

@Entity
public class Exam extends Assignment {
    public enum ExamType {
        TYPE1;
    }
    ExamType type;

    public Exam(long id, long user, int course, List<Long> students, int subject, Date editDate, Date startDate,
            Date dueDate, Workload workload, ExamType type) {
        super(id, user, course, students, subject, editDate, startDate, dueDate, workload);
        this.type = type;
    }

    public ExamType getType() {
        return type;
    }

    public void setType(ExamType type) {
        this.type = type;
    }
    
}
