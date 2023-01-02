package pl.lodz.p.it.opinioncollector.productManagment;


import jakarta.persistence.*;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.lodz.p.it.opinioncollector.category.model.Category;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Data
@Entity
@Valid
@NoArgsConstructor
public class Product implements Serializable {

    @Id
    @Column(name = "product_ID", updatable = false, nullable = false)
    @Setter(AccessLevel.NONE)
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID productId;

    @ManyToOne(optional = false) //cascade?
    @JoinColumn(name = "category_id", referencedColumnName = "categoryid", nullable = false)
    private Category category;

    private String name;

    private String description;

    private boolean deleted;

    private boolean confirmed;

    @ElementCollection
    @CollectionTable(name = "properties")
    @MapKeyColumn(name = "key")
    @Column(name = "value")
    @JoinColumn(name = "product_id")
    private Map<String, String> properties = new HashMap<>();


    public Product(UUID categoryId, String name, String description) {
        this.name = name;
        this.description = description;
        this.deleted = false;
        this.confirmed = true;
    }

    public Product(ProductDTO productDTO) {
        this.name = productDTO.getName();
        this.description = productDTO.getDescription();
        this.deleted = false;
        this.confirmed = true;
    }

    public void removeProperty(String key) {
        properties.remove(key); //TODO where to use that?
    }

    public void addProperty(String key, String value) {
        properties.put(key, value);
    }

    public String getProperty(String key) {
        return properties.get(key);
    }

    public void mergeProduct(ProductDTO productDTO) {
        this.name = productDTO.getName();
        this.description = productDTO.getDescription();
    }
}
