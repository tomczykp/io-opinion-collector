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

    @Setter(AccessLevel.NONE)
    @Id
    private UUID productId; //fixme generated in db or constructor?

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

        productId = UUID.randomUUID();
        this.categoryId = categoryId;
        this.name = name;
        this.description = description;
        this.deleted = false;
        this.confirmed = true;
        this.properties = properties;
    }

    public void removeProperty(String key) {
        properties.remove(key);
    }
}
