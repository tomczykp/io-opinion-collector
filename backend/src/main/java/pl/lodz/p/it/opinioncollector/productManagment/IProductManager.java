package pl.lodz.p.it.opinioncollector.productManagment;

import java.util.List;
import java.util.UUID;

public interface IProductManager {

//    Product createProduct(ProductDTO productDTO);

//    Product createSuggestion(ProductDTO productDTO);

    Product getProduct(UUID uuid);

//    Product updateProduct(UUID uuid, ProductDTO productDTO);

    boolean confirmProduct(UUID uuid);

    boolean unconfirmProduct(UUID uuid);

//    boolean deleteProduct(UUID uuid);

    List<Product> getAllProducts();

    List<Product> getProductsByCategory(UUID uuid);

    List<Product> getUnconfirmedSuggestions();
}
