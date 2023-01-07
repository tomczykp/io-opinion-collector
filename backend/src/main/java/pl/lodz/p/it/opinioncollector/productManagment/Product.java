package pl.lodz.p.it.opinioncollector.productManagment;


import jakarta.persistence.*;
import jakarta.validation.Valid;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.lodz.p.it.opinioncollector.category.model.Category;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Data
@Entity
@Valid
@NoArgsConstructor
public class Product implements Serializable {

    @Id
    @Column(name = "product_ID", nullable = false)
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID productId;

    private UUID constantProductId;

    @ManyToOne(optional = false) //cascade?
    @JoinColumn(name = "category_id", referencedColumnName = "categoryid", nullable = false)
    private Category category;

    private String name;

    private String description;

    private boolean deleted;

    private boolean confirmed;

    private LocalDateTime createdAt;

//    private LocalDateTime editedAt;

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
        this.confirmed = false;
        this.setCreatedAt(LocalDateTime.now());
    }

    public Product(Product product) {
        this.productId = product.productId;
        this.constantProductId = product.constantProductId;
        this.category = product.category;
        this.name = product.name;
        this.description = product.description;
        this.deleted = product.deleted;
        this.confirmed = product.confirmed;
        this.createdAt = product.createdAt;
        this.properties = product.properties;
    }

    public void addProperty(String key, String value) {
        properties.put(key, value);
    }

    public String getProperty(String key) {
        return properties.get(key);
    }

    public void setProperties(Map<String, String> properties) {
        for (String key :
                properties.keySet()) {
            this.properties.put(key, properties.get(key)); //FIXME but now key has to be unique, hashset?
        }
//            this.properties.putIfAbsent(key, properties.get(key)); // How about that?
//            if(this.properties.putIfAbsent(key, properties.get(key)) == null){
//            this.properties.put(key + "idk_something to distinct", properties.get(key));
//            }  // And this?
    }

}
