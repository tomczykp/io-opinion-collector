package pl.lodz.p.it.opinioncollector.category;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;

public class FieldManager {
    private List<Field> fields;

    public FieldManager()
    {
        fields = new ArrayList<Field>();
    }

    public Field createField(String name, Class type)
    {
        Field f = new Field(UUID.randomUUID(),name,type);
        fields.add(f);
        return f;
    }

    public Field getField(UUID fieldID)
    {
        for(Field f:fields){
            if(fieldID == f.getFieldID()){
                return f;
            }
        }
        return null;
    }

    public List<Field> getFields(Predicate<Field> Predicate)
    {
        List<Field> result = new ArrayList<Field>();
        for(Field f: fields){
            if(Predicate.test(f))
                result.add(f);
        }
        return result;
    }

    public boolean deleteField(String name)
    {
        for(Field f: fields){
            if(f.getName() == name){
                return fields.remove(f);
            }
        }
        return false;
    }
}
