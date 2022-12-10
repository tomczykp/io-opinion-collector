package pl.lodz.p.it.opinioncollector.category;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;

public class FieldManager {
    private FieldRepository fields;

    public FieldManager(FieldRepository fieldRepository)
    {
        fields = fieldRepository;
    }

    public Field createField(String name, Class type)
    {
        Field f = new Field(UUID.randomUUID(),name,type);
        fields.save(f);
        return f;
    }

    public Field getField(UUID fieldID)
    {
        return fields.getById(fieldID);
    }

    public List<Field> getFields(Predicate<Field> Predicate)
    {
        List<Field> allFields = fields.findAll();
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
}
