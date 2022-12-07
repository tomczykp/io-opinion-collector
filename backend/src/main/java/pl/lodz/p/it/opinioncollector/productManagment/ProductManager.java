package pl.lodz.p.it.opinioncollector.productManagment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ProductManager {
    private final ProductRepository productRepository;

    @Autowired
    public ProductManager(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Product createProduct(UUID categoryId, String name, String description, HashMap<String, String> properties) {
        Product product = new Product(categoryId, name, description, properties);  //TODO DTO??
        productRepository.save(product);
        return product;
    }

    public Product createSuggestion(Product product) { //TODO DTO??
        product.setConfirmed(false);     //TODO Maybe copying constructor?
        productRepository.save(product); //FIXME Is the same UUID not a problem??
        return product;
    }


    public Product updateProduct(Product product) {
        Optional<Product> productOptional = productRepository.findById(product.getProductId());
        if (productOptional.isPresent()) {
            productRepository.save(product);
        }
        return product;
    }

    public boolean deleteProduct(UUID uuid) {
        Optional<Product> product = productRepository.findById(uuid);
        if (product.isPresent()) {
            product.get().setDeleted(true);
            productRepository.save(product.get());
            return true;
        }
        return false;
    }

    public Optional<Product> getProduct(UUID uuid) {
        return productRepository.findById(uuid);
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public List<Product> getProductsByCategory(UUID uuid) {
        return productRepository.findByCategoryId(uuid);
    }

    public List<Product> getUnconfirmedSuggestions() {
        return productRepository.findByConfirmedFalse();
    }
}
