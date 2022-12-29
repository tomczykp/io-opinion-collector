package pl.lodz.p.it.opinioncollector.category.model;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.lodz.p.it.opinioncollector.category.model.dto.CategoryDTO;
import pl.lodz.p.it.opinioncollector.exceptions.category.UnsupportedTypeException;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Data
@Entity
@Valid
@NoArgsConstructor
@AllArgsConstructor
public class Category {
    @Id
    @Column(name = "categoryID")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID categoryID;

    @OneToOne(orphanRemoval = false, optional = true)
    @JoinColumn(name = "parent_category_category_id")
    private Category parentCategory;

    private String name;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE })
    @JoinTable(name = "category_fields",
            joinColumns = @JoinColumn(name = "category_categoryid"),
            inverseJoinColumns = @JoinColumn(name = "fields_fieldid"))
    private List<Field> fields = new ArrayList<>();

    public Category getParentCategory() {
        return parentCategory;
    }

    public void setParentCategory(Category parentCategory) {
        this.parentCategory = parentCategory;
    }

    public Category(String name, List<Field> fields) {
        this.name = name;
        this.fields = fields;
    }

    public Category(CategoryDTO categoryDTO) throws UnsupportedTypeException {
        this.name = categoryDTO.getName();
        for(int i=0; i<categoryDTO.getFields().size(); i++){
            fields.add(new Field(categoryDTO.getFields().get(i)));
        }
    }

}
