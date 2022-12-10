package pl.lodz.p.it.opinioncollector.category;

import jakarta.persistence.Column;
import jakarta.persistence.Id;

import java.util.List;
import java.util.UUID;

public class Category {
    @Id
    @Column(name = "categoryID")
    private final UUID categoryID;
    private UUID parentCategoryID;
    private String name;
    private List<Field> fields;

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
