package pl.lodz.p.it.opinioncollector.productManagment;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.MapKeyColumn;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashMap;
import java.util.UUID;

@Data
@NoArgsConstructor
public class ProductDTO {

//    @Setter(AccessLevel.NONE)
//    private UUID productId;
//
//    @Setter(AccessLevel.NONE)
//    private UUID categoryId;
//
//    private String name;
//
//    private String description;
//
//    @ElementCollection
//    @MapKeyColumn(name = "KEY")
//    @Column(name = "VALUE")
//    @CollectionTable(name = "PROPERTIES")
//    private HashMap<String, String> properties;

    //FIXME It's gonna be a problem with those properties?
}
