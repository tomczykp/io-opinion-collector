package pl.lodz.p.it.opinioncollector.productManagment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import pl.lodz.p.it.opinioncollector.category.managers.CategoryManager;
import pl.lodz.p.it.opinioncollector.category.model.Field;
import pl.lodz.p.it.opinioncollector.eventHandling.IProductEventManager;
import pl.lodz.p.it.opinioncollector.exceptions.category.CategoryNotFoundException;
import pl.lodz.p.it.opinioncollector.exceptions.products.ProductCannotBeEditedException;
import pl.lodz.p.it.opinioncollector.userModule.user.User;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class ProductManager implements IProductManager {
    private final ProductRepository productRepository;
    private final IProductEventManager eventManager;
    private final CategoryManager categoryManager;

    @Autowired
    public ProductManager(ProductRepository productRepository, IProductEventManager eventManager,
                          CategoryManager categoryManager) {
        this.productRepository = productRepository;
        this.eventManager = eventManager;
        this.categoryManager = categoryManager;
    }

    // unused
    public Product createProduct(ProductDTO productDTO) {
        Product product = new Product(productDTO);
        productRepository.save(product);
        return product;
    }


    public Product createSuggestion(ProductDTO productDTO) throws CategoryNotFoundException {
        Product product = new Product(productDTO);

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        try {
            product.setCategory(categoryManager.getCategory(productDTO.getCategoryId()));

        } catch (CategoryNotFoundException e) {
            throw new CategoryNotFoundException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        // Putting properties from DTO to a new product
        if (productDTO.properties != null) {
            for (Map.Entry<String, String> entry : productDTO.properties.entrySet()) {
                product.addProperty(entry.getKey(), entry.getValue());
            }
        }

        product.setConstantProductId(product.getProductId());

        productRepository.save(product);
        eventManager.createProductSuggestionEvent(user, "New product suggestion with name: \""
                        + product.getName() + "\" and description: \"" + product.getDescription() + "\"",
                product.getProductId());
        return product;
    }

    public Product getProduct(UUID uuid) {
        Optional<Product> product = productRepository.findById(uuid);
        return product.orElse(null);

    }

    public Product updateProduct(UUID uuid, ProductDTO productDTO) throws ProductCannotBeEditedException {
        Optional<Product> originalProduct = productRepository.findById(uuid);
        if (originalProduct.isPresent()) {
            Product oldProduct = originalProduct.get();
            if (oldProduct.isDeleted()) {
                throw new ProductCannotBeEditedException();
            }
            Product newProduct = new Product(productDTO);

            newProduct.setCategory(oldProduct.getCategory());
            if(productDTO.getProperties() != null) {
                newProduct.setProperties(productDTO.getProperties());
            }

            newProduct.setConstantProductId(oldProduct.getConstantProductId());


//            originalProduct.get().setEditedAt(LocalDateTime.now());

            productRepository.save(oldProduct);
            User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            eventManager.createProductReportEvent(user,
                    "User requested update of product: " + productDTO, oldProduct.getProductId());
            productRepository.save(newProduct);
            return newProduct;

        }
        return null;
    }

    public boolean confirmProduct(UUID uuid) {
        Optional<Product> suggestionOptional = productRepository.findById(uuid);
        if (suggestionOptional.isEmpty()) {
            return false;
        }
        Product suggestion = suggestionOptional.get();

        if(suggestion.isConfirmed()) {
            return false;
        }
        Optional<Product> productOptional = productRepository.findById(suggestion.getConstantProductId());
        if(productOptional.isEmpty()) { //
            suggestion.setConfirmed(true);
            productRepository.save(suggestion);
            return true;
        }

        Product product = productOptional.get();
        suggestion.setConfirmed(true);
        product.setDeleted(true);

        swapProducts(product, suggestion);

        productRepository.save(product);
        productRepository.save(suggestion);
        return true;
    }

    private void swapProducts(Product p1, Product p2) {
        Product temp = new Product(p1);

        p1.setConstantProductId(p2.getConstantProductId());
        p1.setCategory(p2.getCategory());
        p1.setName(p2.getName());
        p1.setDescription(p2.getDescription());
        p1.setDeleted(p2.isDeleted());
        p1.setConfirmed(p2.isConfirmed());
        p1.setCreatedAt(p2.getCreatedAt());
        p1.setProperties(p2.getProperties());

        p2.setConstantProductId(temp.getConstantProductId());
        p2.setCategory(temp.getCategory());
        p2.setName(temp.getName());
        p2.setDescription(temp.getDescription());
        p2.setDeleted(temp.isDeleted());
        p2.setConfirmed(temp.isConfirmed());
        p2.setCreatedAt(temp.getCreatedAt());
        p2.setProperties(temp.getProperties());
    }

    public boolean unconfirmProduct(UUID uuid) {
        Optional<Product> productOptional = productRepository.findById(uuid);
        if (productOptional.isPresent()) {
            productOptional.get().setConfirmed(false);
            productRepository.save(productOptional.get());
            return true;
        }
        return false;
    }

    public boolean makeDeleteFormProduct(UUID uuid, ProductDeleteForm productDF) {
        Optional<Product> productOptional = productRepository.findById(uuid);
        if (productOptional.isPresent()) {
            User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

            Product product = getProduct(uuid);

            eventManager.createProductReportEvent(user,"User requested deletion of" +
                    " a product: " + product.getName() + " with description: \"" + productDF.getDescription() + "\"", uuid);
            return true;
        }
        return false;
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

    public boolean hardDeleteSuggestion(UUID uuid) {
        Optional<Product> product = productRepository.findById(uuid);
        if (product.isPresent() && !product.get().isConfirmed()) {
            productRepository.delete(product.get());
            return true;
        }
        return false;
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public List<Product> getProductsByCategory(UUID uuid) {
        return productRepository.findByCategoryCategoryID(uuid);
    }

    public List<Product> getUnconfirmedSuggestions() {
        return productRepository.findByConfirmedFalse();
    }

    public List<Product> getConfirmedNotDeletedSuggestions() {
        return productRepository.findProductsByConfirmedTrueAndDeletedFalse();
    }

    public List<Product> getProductsByConstantId(UUID uuid) {
        return productRepository.findByConstantProductId(uuid);
    }


    //FIXME both are not working
//    public List<Product> getByPropertyValues(String value) {
//        return productRepository.getByPropertyValues(value);
//    }

//    public List<Product> getByPropertyKey(String key) {
//        return productRepository.getByPropertyKey(key);
//    }
}
