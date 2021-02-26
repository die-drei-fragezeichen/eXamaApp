package ch.diedreifragezeichen.exama.assignments;

import java.util.Date;

public class Homework extends Assignment {
    public Homework(long id, long user, int course, int subject, Date editDate, Date startDate,
            Date dueDate, Workload workload) {
        super(id, user, course, subject, editDate, startDate, dueDate, workload);
    }

    public Homework(long id, long user, int course, int subject, Date editDate,
            Date dueDate, Workload workload) {
        super(id, user, course, subject, editDate, editDate, dueDate, workload);
    }
}
