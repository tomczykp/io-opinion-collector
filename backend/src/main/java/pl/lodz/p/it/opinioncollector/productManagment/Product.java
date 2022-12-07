package pl.lodz.p.it.opinioncollector.productManagment;


import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashMap;
import java.util.UUID;

@Data
@Entity
@NoArgsConstructor
public class Product {

    @Id
    @Setter(AccessLevel.NONE)
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID productId;

    @Setter(AccessLevel.NONE)
    private UUID categoryId;

    private String name;

    private String description;

    private boolean deleted;

    private boolean confirmed;

    @ElementCollection
    @MapKeyColumn(name = "KEY")
    @Column(name = "VALUE")
    @CollectionTable(name = "PROPERTIES")
    private HashMap<String, String> properties;

    public Product(UUID categoryId, String name, String description,  HashMap<String, String> properties) {
        this.categoryId = categoryId;
        this.name = name;
        this.description = description;
        this.deleted = false;
        this.confirmed = true;
        this.properties = properties;
    }

    public Product(ProductDTO productDTO) {
        this.categoryId = productDTO.getCategoryId();
        this.name = productDTO.getName();
        this.description = productDTO.getDescription();
        this.deleted = false;
        this.confirmed = productDTO.isConfirmed();
        this.properties = productDTO.getProperties();
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
        this.categoryId = productDTO.getCategoryId();
        this.name = productDTO.getName();
        this.description = productDTO.getDescription();
        this.properties = productDTO.getProperties();
    }
}
