package ch.diedreifragezeichen.exama.courses;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<Course, Long> {
    public Course findCourseById(Long id);

    public CoreCourse findCoreCourseById(Long coreCourseId);
}
