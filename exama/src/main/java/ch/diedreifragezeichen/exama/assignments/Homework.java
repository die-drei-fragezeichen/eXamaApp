package ch.diedreifragezeichen.exama.assignments;

import java.util.Date;
import java.util.List;

public class Homework extends Assignment {
    public Homework(long id, long user, int course, List<Long> students, int subject, Date editDate, Date startDate,
            Date dueDate, Workload workload) {
        super(id, user, course, students, subject, editDate, startDate, dueDate, workload);
    }

    public Homework(long id, long user, int course, List<Long> students, int subject, Date editDate,
            Date dueDate, Workload workload) {
        super(id, user, course, students, subject, editDate, editDate, dueDate, workload);
    }
}
