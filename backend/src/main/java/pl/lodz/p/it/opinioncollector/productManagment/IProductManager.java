package pl.lodz.p.it.opinioncollector.productManagment;

import java.util.List;
import java.util.UUID;

public interface IProductManager {

    Product getProduct(UUID uuid);

    boolean confirmProduct(UUID uuid);

    boolean unconfirmProduct(UUID uuid);

    List<Product> getAllProducts();

    List<Product> getProductsByCategory(UUID uuid);

    List<Product> getUnconfirmedSuggestions();
}
