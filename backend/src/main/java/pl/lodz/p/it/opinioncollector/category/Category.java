package pl.lodz.p.it.opinioncollector.category;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Data
@Entity
@Valid
@NoArgsConstructor
public class Category {
    @Id
    @Column(name = "categoryID")
    private UUID categoryID;

    private UUID parentCategoryID;
    private String name;
    @ElementCollection
    private List<Field> fields = new ArrayList<>();

    public Category(UUID categoryID, String name, List<Field> fields) {
        this.categoryID = categoryID;
        this.name = name;
        this.fields = fields;
    }

    public UUID getCategoryID() {
        return categoryID;
    }

    public UUID getParentCategoryID() {
        return parentCategoryID;
    }

    public void setParentCategoryID(UUID parentCategoryID) {
        this.parentCategoryID = parentCategoryID;
    }

    public String getName() {
        return name;
    }

    public List<Field> getFields() {
        return fields;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setFields(List<Field> fields) {
        this.fields = fields;
    }
}
