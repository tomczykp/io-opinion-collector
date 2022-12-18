package pl.lodz.p.it.opinioncollector.category;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.lang.reflect.Constructor;
import java.util.LinkedHashSet;
import java.util.Set;
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
    private String type;

    @JsonIgnore
    @ManyToMany(mappedBy = "fields")
    private Set<Category> categories = new LinkedHashSet<>();

    public Set<Category> getCategories() {
        return categories;
    }

    public void setCategories(Set<Category> categories) {
        this.categories = categories;
    }

    public Field(FieldDTO dto){
        //this.fieldID = UUID.randomUUID();
        try {
            this.name = dto.getName();
            this.type = dto.getType();//Class.forName(dto.getType());
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

    public String getType() {
        return type;
    }


}