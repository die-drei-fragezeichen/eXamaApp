package ch.diedreifragezeichen.exama.subject;
import javax.persistence.*;

//the following code maps with the corresponding subject table (not yet created) in the database.
//then we create a new interface named UserRepository to act as a Spring Data JPA repository with the following simple code
//the class is mapped to the table subjects in the database using the annotations @Entity.
@Entity
@Table(name = "subjects")
public class Subject {
    //The @Id and @GeneratedValue annotations map the field id to the primary key column of the table. 
    @Id
    @Column(name = "subject_id", unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "subject_name", unique = true, nullable = false, length = 20)
    private String subjectName;

    @Column(name = "subject_tag", unique = true, nullable = false, length = 4)
    private String subjectTag;
    

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getSubjectTag() {
        return subjectTag;
    }

    public void setSubjectTag(String subjectTag) {
        this.subjectTag = subjectTag;
    }

}    
