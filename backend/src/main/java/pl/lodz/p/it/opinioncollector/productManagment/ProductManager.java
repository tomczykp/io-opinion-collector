package pl.lodz.p.it.opinioncollector.productManagment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import pl.lodz.p.it.opinioncollector.category.managers.CategoryManager;
import pl.lodz.p.it.opinioncollector.category.model.Field;
import pl.lodz.p.it.opinioncollector.eventHandling.IProductEventManager;
import pl.lodz.p.it.opinioncollector.exceptions.category.CategoryNotFoundException;
import pl.lodz.p.it.opinioncollector.userModule.user.User;

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
        for (Map.Entry<String, String> entry : productDTO.properties.entrySet()) {
            product.addProperty(entry.getKey(), entry.getValue());
        }

        product.setConstantProductId(UUID.randomUUID());

        productRepository.save(product);
        eventManager.createProductReportEvent(user, "New product suggestion with name: \""
                        + product.getName() + "\" and description: \"" + product.getDescription() + "\"",
                product.getProductId());
        return product;
    }

    public Product getProduct(UUID uuid) {
        Optional<Product> product = productRepository.findById(uuid);
        return product.orElse(null);
        //            if (rProduct.getProperties().isEmpty()) {
        //                List<Field> fields = product.get().getCategory().getFields();
        //                Map<String, String> properties = new HashMap<>();
        //
        //                for (Field f :
        //                        fields) {
        //                    properties.put(f.getName(), f.getType());
        //                }
        //                rProduct.setProperties(properties);
        //            }

    }

    public Product updateProduct(UUID uuid, ProductDTO productDTO) {
        Optional<Product> originalProduct = productRepository.findById(uuid);
        if (originalProduct.isPresent()) {
            Product newProduct = new Product(productDTO);

            newProduct.setCategory(originalProduct.get().getCategory());
            newProduct.setProperties(productDTO.getProperties());
            newProduct.setConstantProductId(originalProduct.get().getConstantProductId());


//            originalProduct.get().setEditedAt(LocalDateTime.now());

            productRepository.save(originalProduct.get());
            User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            eventManager.createProductReportEvent(user,
                    "User requested update of product: " + productDTO, originalProduct.get().getProductId());
            productRepository.save(newProduct);
            return newProduct;

        }
        return null;
    }

    public boolean confirmProduct(UUID uuid) {
        Optional<Product> productOptional = productRepository.findById(uuid);
        if (productOptional.isPresent()) {
            productOptional.get().setConfirmed(true);
            productRepository.save(productOptional.get());
            return true;
        }
        return false;
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


            eventManager.createProductReportEvent(user,"User requested deletion of" +
                    " a product with description: \"" + productDF.getDescription() + "\"", uuid);
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

    public List<Product> getLatestVersionProduct(UUID uuid) {
        return productRepository.findByConstantProductIdAndDeletedFalse(uuid);
    }


    //FIXME both are not working
//    public List<Product> getByPropertyValues(String value) {
//        return productRepository.getByPropertyValues(value);
//    }

//    public List<Product> getByPropertyKey(String key) {
//        return productRepository.getByPropertyKey(key);
//    }
}
