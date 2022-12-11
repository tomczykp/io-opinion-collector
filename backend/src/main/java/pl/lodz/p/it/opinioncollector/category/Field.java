package pl.lodz.p.it.opinioncollector.category;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.Valid;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;
@Data
@Entity
@Valid
@NoArgsConstructor
public class Field {
    @Id
    @Column(name = "fieldID")
    private UUID fieldID;
    private String name;
    private Class type;
    public Field(UUID fieldID, String name, Class type)
    {
        this.fieldID = fieldID;
        this.name = name;
        this.type = type;
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
