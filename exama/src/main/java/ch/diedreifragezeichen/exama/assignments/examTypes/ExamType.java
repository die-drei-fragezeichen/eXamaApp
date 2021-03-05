package ch.diedreifragezeichen.exama.assignments.examTypes;

import javax.persistence.*;

import org.hibernate.annotations.DynamicUpdate;

@Entity
@DynamicUpdate
@Table(name = "examtypes")
public class ExamType {

    @Id
    @Column(name = "examtype_id", unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "examtype_name", unique = true, nullable = false, length = 50)
    private String name;

    @Column(name = "examtype_timevalue", unique = false, nullable = false)
    private double timeValue;

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        ExamType examType = (ExamType) obj;
        if (this.id == examType.getId()) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.id == null) ? 0 : this.id.hashCode());
        return result;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getTimeValue() {
        return this.timeValue;
    }

    public void setTimeValue(double timeValue) {
        this.timeValue = timeValue;
    }
}
