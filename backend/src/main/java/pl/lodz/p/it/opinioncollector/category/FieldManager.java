package pl.lodz.p.it.opinioncollector.category;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.lodz.p.it.opinioncollector.category.model.Field;
import pl.lodz.p.it.opinioncollector.category.model.dto.FieldDTO;
import pl.lodz.p.it.opinioncollector.category.repositories.FieldRepository;
import pl.lodz.p.it.opinioncollector.exceptions.category.FieldNotFoundException;
import pl.lodz.p.it.opinioncollector.exceptions.category.UnsupportedTypeException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Predicate;
@Service
public class FieldManager {
    private FieldRepository fieldRepository;
    @Autowired
    public FieldManager(FieldRepository fieldRepository)
    {
        this.fieldRepository = fieldRepository;
    }

    public Field createField(FieldDTO fieldDTO) throws UnsupportedTypeException {
        Field f = new Field(fieldDTO);
        fieldRepository.save(f);
        return f;
    }

    public Field getField(UUID fieldID)
    {
        return fieldRepository.getById(fieldID);
    }

    public List<Field> getFields(Predicate<Field> Predicate)
    {
        List<Field> allFields = fieldRepository.findAll();
        List<Field> result = new ArrayList<Field>();
        for(Field f: allFields){
            if(Predicate.test(f))
                result.add(f);
        }
        return result;
    }

    public void deleteField(String name)
    {
        deleteField(name);
    }

    public Field updateField(UUID uuid, FieldDTO fieldDTO) throws FieldNotFoundException, UnsupportedTypeException {
        Optional<Field> fieldOptional = fieldRepository.findById(uuid);
        if(fieldOptional.isPresent()){
            fieldOptional.get().setName(fieldDTO.getName());
            fieldOptional.get().setType(fieldDTO.getType());
            fieldRepository.save(fieldOptional.get());
            return fieldOptional.get();
        } else {
            throw new FieldNotFoundException(uuid.toString());
        }
    }
}
