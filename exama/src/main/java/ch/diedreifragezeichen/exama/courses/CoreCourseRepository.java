package ch.diedreifragezeichen.exama.courses;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CoreCourseRepository  extends JpaRepository<CoreCourse, Long> {
    public CoreCourse findCoreCourseById(Long id);
}
