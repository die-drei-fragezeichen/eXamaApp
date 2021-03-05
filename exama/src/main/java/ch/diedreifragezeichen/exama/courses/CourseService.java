package ch.diedreifragezeichen.exama.courses;

import javax.persistence.*;

import org.springframework.beans.factory.annotation.Autowired;

import javassist.NotFoundException;

public class CourseService {
    @Autowired
    private CourseRepository courseRepo;

    @PersistenceContext
    EntityManager em;

    public Course loadById(long id) throws NotFoundException {
        Course course = courseRepo.getCourseById(id);
        if (course == null) {
            throw new NotFoundException("Course not found");
        }
        return course;
    }
}