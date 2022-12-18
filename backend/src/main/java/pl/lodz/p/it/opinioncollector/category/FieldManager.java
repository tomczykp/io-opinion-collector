package pl.lodz.p.it.opinioncollector.category;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.server.ResponseStatusException;
import pl.lodz.p.it.opinioncollector.exceptions.category.FieldNotFoundException;

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

    public Field createField(FieldDTO fieldDTO){
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

    public Field updateField(UUID uuid, FieldDTO fieldDTO) throws FieldNotFoundException {
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
