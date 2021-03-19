package ch.diedreifragezeichen.exama.courses;

import javax.persistence.*;
import javax.transaction.Transactional;

import org.hibernate.Session;

import org.springframework.stereotype.Service;

@Service("CoreCourseService")
public class CoreCourseService{

    @PersistenceContext
    private EntityManager em;

    @Transactional
    public void saveOrUpdateCoreCourse(CoreCourse coreCourse){
        Session session = em.unwrap(org.hibernate.Session.class);
        session.evict(coreCourse);
        session.saveOrUpdate(coreCourse);
    }
}
