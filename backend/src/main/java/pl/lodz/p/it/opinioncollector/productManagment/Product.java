package pl.lodz.p.it.opinioncollector.productManagment;


import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;

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
    @Setter(AccessLevel.NONE)
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID productId;

    @Setter(AccessLevel.NONE)
    private UUID categoryId;

    private String name;

    private String description;

    private boolean deleted;

    private boolean confirmed;

    @NotNull
    @ElementCollection
    @CollectionTable(name = "properties")
    @MapKeyColumn(name = "key")
    @Column(name = "value")
    @JoinColumn(name = "productId")
    private Map<String, String> properties;


    public Product(UUID categoryId, String name, String description, HashMap<String, String> properties) {
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
        this.confirmed = true;
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
