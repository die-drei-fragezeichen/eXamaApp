package ch.diedreifragezeichen.exama.subject;

import javax.persistence.*;

import org.hibernate.annotations.DynamicUpdate;

//the following code maps with the corresponding subject table (not yet created) in the database.
//then we create a new interface named UserRepository to act as a Spring Data JPA repository with the following simple code
//the class is mapped to the table subjects in the database using the annotations @Entity.
@Entity
@DynamicUpdate
@Table(name = "subjects")
public class Subject {
    // The @Id and @GeneratedValue annotations map the field id to the primary key
    // column of the table.
    @Id
    @Column(name = "subject_id", unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "subject_name", unique = true, nullable = false, length = 20)
    private String name;

    @Column(name = "subject_tag", unique = true, nullable = false, length = 4)
    private String tag;

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        Subject subject = (Subject) obj;
        if (this.id == subject.getId()) {
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

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

}