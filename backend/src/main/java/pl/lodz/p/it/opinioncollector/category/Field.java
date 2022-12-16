package pl.lodz.p.it.opinioncollector.category;


import jakarta.persistence.*;
import jakarta.validation.Valid;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.lang.reflect.Constructor;
import java.util.UUID;
@Data
@Entity
@Valid
@Embeddable
@NoArgsConstructor
public class Field {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "fieldID")
    private UUID fieldID;
    private String name;
    private Class type;

    public Field(FieldDTO dto){
        //this.fieldID = UUID.randomUUID();
        try {
            this.name = dto.getName();
            this.type = Class.forName(dto.getType());
        }catch(Exception e){
            System.out.println(e);
        }
    }
    public UUID getFieldID() {
        return fieldID;
    }

    public String getName() {
        return name;
    }

    public Class getType() {
        return type;
    }
}