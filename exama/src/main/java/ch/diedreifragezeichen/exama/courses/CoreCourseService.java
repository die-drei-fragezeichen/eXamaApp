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


    //     public CoreCourse saveOrUpdateCoreCourse(CoreCourse coreCourse){
    //         if(coreCourse.getId()==null){
    //             session.persist(coreCourse);
    //             //upon closing the session, the coreCourse will receive a unique id. Right now, it is in "limbo"
    //         } else{
    //             session.evict(coreCourse);
    //             //once evecited (=detached) from its context, pass the coreCourse into an HTML environment (or statically update a field in the backend)
    //             //do this by creating an appropriate controller method
    //             coreCourse = (CoreCourse) session.merge(coreCourse);
    //         }
    //         return coreCourse;
    }
}
