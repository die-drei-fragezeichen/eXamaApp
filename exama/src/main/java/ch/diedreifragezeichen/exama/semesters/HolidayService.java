package ch.diedreifragezeichen.exama.semesters;

import javax.persistence.*;
import javax.transaction.Transactional;
    
import org.springframework.stereotype.Service;

 @Service("HolidayService")
    public class HolidayService {
    
        @PersistenceContext
        private EntityManager em;
    
        @Transactional
        public void saveOrUpdateHoliday(Holiday holiday) {
            em.unwrap(org.hibernate.Session.class).saveOrUpdate(holiday);
            // .unwrap provides the current Session via JPA, as provided by the
            // entityManager. this session then successfully
            // perform update or new entity when called.
        }
    }
    
