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

    public Product createProduct(ProductDTO productDTO) {
        Product product = new Product(productDTO);
        productRepository.save(product);
        return product;
    }

    public Product createSuggestion(Product product) {
        product.setConfirmed(false);
        productRepository.save(product);
        return product;
    }

    public Product createSuggestion(ProductDTO productDTO) {
        Product product = new Product(productDTO);
        product.setConfirmed(false);
        productRepository.save(product);
        return product;
    }

    public Product getProduct(UUID uuid) {
        Optional<Product> product = productRepository.findById(uuid);
        if (product.isPresent()) {
            return product.get();
        }
        return null;
    }

    public Product updateProduct(Product product) {
        Optional<Product> productOptional = productRepository.findById(product.getProductId());
        if (productOptional.isPresent()) {
            productRepository.save(product);
        }
        return product;
    }
    public Product updateProduct(UUID uuid, ProductDTO productDTO) {
        Optional<Product> productOptional = productRepository.findById(uuid);
        if (productOptional.isPresent()) {
            productOptional.get().mergeProduct(productDTO);
            productRepository.save(productOptional.get());
            return productOptional.get();
        }
        return null; //fixme ?
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
