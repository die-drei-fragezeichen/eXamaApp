package ch.diedreifragezeichen.exama.courses;

import javax.persistence.*;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("CoreCourseService")
public class CoreCourseService{
    @Autowired
    private CoreCourseRepository coreCourseRepo;
    @PersistenceContext
    private EntityManager em;

    @Transactional
    public void saveOrUpdateCoreCourse(CoreCourse coreCourse){
        if(coreCourse.getId()==null){
            em.persist(coreCourse);
        } else{
            CoreCourse updatedCoreCourse = coreCourseRepo.findCoreCourseById(coreCourse.getId());
            updatedCoreCourse.setName(coreCourse.getName());
            updatedCoreCourse.setEnabled(coreCourse.isEnabled());
            updatedCoreCourse.setClassTeacher(coreCourse.getClassTeacher());
        }
    }
}
