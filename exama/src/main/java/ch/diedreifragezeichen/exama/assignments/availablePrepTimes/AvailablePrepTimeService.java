package ch.diedreifragezeichen.exama.assignments.availablePrepTimes;

import javax.persistence.*;

import org.springframework.beans.factory.annotation.Autowired;

import javassist.NotFoundException;

public class AvailablePrepTimeService {
    @Autowired
    private AvailablePrepTimeRepository prepTimeRepo;

    @PersistenceContext
    EntityManager em;

    public AvailablePrepTime loadById(long id) throws NotFoundException{
        AvailablePrepTime prepTime = prepTimeRepo.getPrepTimeByID(id);
        if(prepTime == null){
            throw new NotFoundException("PrepTime not found");
        }
        return prepTime;
    }

    public AvailablePrepTime loadByName(String name) throws NotFoundException{
        AvailablePrepTime prepTime = prepTimeRepo.getPrepTimeByName(name);
        if(prepTime == null){
            throw new NotFoundException("PrepTime not found");
        }
        return prepTime;
    }

    public AvailablePrepTime loadByDays(int days) throws NotFoundException{
        AvailablePrepTime prepTime = prepTimeRepo.getPrepTimeByDays(days);
        if(prepTime == null){
            throw new NotFoundException("PrepTime not found");
        }
        return prepTime;
    }
    
}
