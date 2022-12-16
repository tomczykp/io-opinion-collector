package pl.lodz.p.it.opinioncollector.category;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
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
@AllArgsConstructor
public class Category {
    @Id
    @Column(name = "categoryID")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID categoryID;

    private UUID parentCategoryID;
    private String name;
    @ElementCollection
    private List<Field> fields = new ArrayList<>();

    public Category(String name, List<Field> fields) {
        this.name = name;
        this.fields = fields;
    }

    public Category(CategoryDTO categoryDTO) {
        this.name = categoryDTO.getName();
        for(int i=0; i<categoryDTO.getFields().size(); i++){
            fields.add(new Field(categoryDTO.getFields().get(i)));
        }
        this.parentCategoryID = categoryDTO.getParentCategoryID();
    }

    public void mergeCategory(CategoryDTO categoryDTO)  {
        this.name = categoryDTO.getName();
        for(int i=0; i<categoryDTO.getFields().size(); i++){
            fields.add(new Field(categoryDTO.getFields().get(i)));
        }
        this.parentCategoryID = categoryDTO.getParentCategoryID();
    }

}
