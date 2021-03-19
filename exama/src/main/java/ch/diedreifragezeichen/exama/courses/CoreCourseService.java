package ch.diedreifragezeichen.exama.courses;

import javax.persistence.*;
import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

@Service("CoreCourseService")
public class CoreCourseService{
    @PersistenceContext
    private EntityManager em;

    @Transactional
    public CoreCourse saveOrUpdateCoreCourse(CoreCourse coreCourse){
        if(coreCourse.getId()==null){
            em.persist(coreCourse);
        } else{
            em.merge(coreCourse);
        }
        return coreCourse;
    }
}
