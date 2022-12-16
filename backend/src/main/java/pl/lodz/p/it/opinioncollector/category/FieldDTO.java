package pl.lodz.p.it.opinioncollector.category;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.lang.reflect.Type;

@Data
@AllArgsConstructor
public class FieldDTO {
    String name;

    String  type;

    public String getName () {
        return name;
    }

    public String getType () {
        return type;
    }

}