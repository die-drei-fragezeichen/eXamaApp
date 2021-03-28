package ch.diedreifragezeichen.exama._tobetrashed;
// package ch.diedreifragezeichen.exama._unused;

// import javax.persistence.*;
// import javax.transaction.Transactional;

// import org.springframework.stereotype.Service;

// import ch.diedreifragezeichen.exama.courses.CoreCourse;

// @Service("CoreCourseService")
// public class CoreCourseService {

//     @PersistenceContext
//     private EntityManager em;

//     @Transactional
//     public void saveOrUpdateCoreCourse(CoreCourse coreCourse) {
//         em.unwrap(org.hibernate.Session.class).saveOrUpdate(coreCourse);
//         // .unwrap provides the current Session via JPA, as provided by the
//         // entityManager. this session then successfully
//         // perform update or new entity when called.
//     }
// }
