package ch.diedreifragezeichen.exama.assignments.workloadDistributions;

import java.util.List;

import javax.persistence.*;

import org.hibernate.annotations.DynamicUpdate;

import ch.diedreifragezeichen.exama.assignments.exams.Exam;

@Entity
@DynamicUpdate
@Table(name = "workloaddistributions")
public class WorkloadDistribution {
    /**
     * Fields
     */
    @Id
    @Column(unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 20)
    private String name;

	/**
	 * OneToMany mappings
	 */
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "workloadDistribution")
	private List<Exam> exams;

	/**
	 * Methods
	 */
    @Override
    public String toString() {
        return this.name;
    }

    // @Override
    // public boolean equals(Object obj) {
    //     if (this == obj) {
    //         return true;
    //     }
    //     if (obj == null) {
    //         return false;
    //     }
    //     WorkloadDistribution dist = (WorkloadDistribution) obj;
    //     if (this.id == dist.getId()) {
    //         return true;
    //     } else {
    //         return false;
    //     }
    // }

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

}
