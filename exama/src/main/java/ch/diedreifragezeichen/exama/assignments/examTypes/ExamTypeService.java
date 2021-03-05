package ch.diedreifragezeichen.exama.assignments.examTypes;

import javax.persistence.*;

import org.springframework.beans.factory.annotation.Autowired;

import javassist.NotFoundException;

public class ExamTypeService {
    @Autowired
    private ExamTypeRepository examTypeRepo;

    @PersistenceContext
    EntityManager em;

    public ExamType loadById(long id) throws NotFoundException {
        ExamType type = examTypeRepo.getExamTypeByID(id);
        if (type == null) {
            throw new NotFoundException("ExamType not found");
        }
        return type;
    }

    public ExamType loadByName(String name) throws NotFoundException {
        ExamType type = examTypeRepo.getExamTypeByName(name);
        if (type == null) {
            throw new NotFoundException("ExamType not found");
        }
        return type;
    }

}
