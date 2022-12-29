package pl.lodz.p.it.opinioncollector.category.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.lodz.p.it.opinioncollector.category.model.dto.FieldDTO;
import pl.lodz.p.it.opinioncollector.exceptions.category.UnsupportedTypeException;

import java.util.LinkedHashSet;
import java.util.List;
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

    public Field(FieldDTO dto) throws UnsupportedTypeException {
        this.name = dto.getName();
        this.setType(dto.getType());
    }

    public void setType(String type) throws UnsupportedTypeException {
        List<String> supportedTypes = List.of(new String[]{"String", "Double", "Int"});
        if (supportedTypes.contains(type)) {
            this.type = type;
        } else {
            throw new UnsupportedTypeException();
        }
    }
}