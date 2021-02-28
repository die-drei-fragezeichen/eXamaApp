package ch.diedreifragezeichen.exama.assignments;


public class Exam extends Assignment {
    public enum ExamType {
        TYPE1;
    }

    ExamType type;

    public ExamType getType() {
        return type;
    }

    public void setType(ExamType type) {
        this.type = type;
    }
    
}
