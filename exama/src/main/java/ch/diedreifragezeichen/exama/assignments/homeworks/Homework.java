package ch.diedreifragezeichen.exama.assignments.homeworks;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicUpdate;

import ch.diedreifragezeichen.exama.assignments.assignment.Assignment;

@Entity
@DynamicUpdate
@Table(name = "homeworks")
public class Homework extends Assignment {

}