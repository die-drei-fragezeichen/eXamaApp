package ch.diedreifragezeichen.exama.assignments.availablePrepTimes;

import javax.persistence.*;

import org.springframework.beans.factory.annotation.Autowired;

import javassist.NotFoundException;

public class AvailablePrepTimeService {
    @Autowired
    private AvailablePrepTimeRepository prepTimeRepo;

    @PersistenceContext
    EntityManager em;

    public AvailablePrepTime loadById(long id) throws NotFoundException {
        AvailablePrepTime preptime = prepTimeRepo.getPrepTimeByID(id);
        if (preptime == null) {
            throw new NotFoundException("PrepTime not found");
        }
        return preptime;
    }

    public void save(AvailablePrepTime preptime){
        prepTimeRepo.save(preptime);
    }

    public void update(AvailablePrepTime preptime){
        em.merge(preptime);
    }

    
}
