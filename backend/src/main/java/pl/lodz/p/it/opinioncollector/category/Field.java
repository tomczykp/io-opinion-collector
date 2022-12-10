package pl.lodz.p.it.opinioncollector.category;

import jakarta.persistence.Column;
import jakarta.persistence.Id;

import java.util.UUID;

public class Field {
    @Id
    @Column(name = "fieldID")
    private final UUID fieldID;
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
