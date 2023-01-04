package pl.lodz.p.it.opinioncollector.productManagment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import pl.lodz.p.it.opinioncollector.category.managers.CategoryManager;
import pl.lodz.p.it.opinioncollector.category.model.Category;
import pl.lodz.p.it.opinioncollector.category.model.Field;
import pl.lodz.p.it.opinioncollector.eventHandling.IProductEventManager;
import pl.lodz.p.it.opinioncollector.exceptions.category.CategoryNotFoundException;
import pl.lodz.p.it.opinioncollector.userModule.user.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

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
        product.setConfirmed(false);


        try {
            product.setCategory(categoryManager.getCategory(productDTO.getCategoryId()));

        } catch (CategoryNotFoundException e) {
            throw new CategoryNotFoundException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        Category categoryTemp = product.getCategory();
        while (categoryTemp != null) {
            List<Field> fields = categoryTemp.getFields();
            for (Field field :
                    fields) {
                product.addProperty(field.getName(), field.getType());
            }
            categoryTemp = categoryTemp.getParentCategory();
        }

        product.setParentProductId(null);
        product.setConstantProductId(UUID.randomUUID());
        productRepository.save(product);
        eventManager.createProductReportEvent(user.getId(), "New product suggestion with name: \""
                        + product.getName() + "\" and description: \"" + product.getDescription() + "\"",
                product.getProductId());
        return product;
    }

    public Product getProduct(UUID uuid) {
        Optional<Product> product = productRepository.findById(uuid);
        return product.orElse(null);
    }

    public Product updateProduct(UUID uuid, ProductDTO productDTO) {
        Optional<Product> originalProduct = productRepository.findById(uuid);
        if (originalProduct.isPresent()) {
            Product newProduct = new Product(productDTO);
            newProduct.setProperties(originalProduct.get().getProperties());
            newProduct.setProperties(productDTO.getProperties());
            newProduct.setParentProductId(originalProduct.get().getParentProductId());
            newProduct.setConstantProductId(originalProduct.get().getConstantProductId()); //FIXME works for now?


//            List<Product> latestVersionProductList = productRepository.
//                    findByConstantProductIdAndDeletedFalse(newProduct.getConstantProductId());
//            if (!latestVersionProductList.isEmpty()) {
//                Product latestVersionProduct = latestVersionProductList.get(0);
//                latestVersionProduct.setDeleted(true); //FIXME
//            }

            User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            eventManager.createProductReportEvent(user.getId(),
                    "User requested update of product: " + productDTO, newProduct.getParentProductId()); //This?
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

    public boolean makeDeleteFormProduct(UUID uuid, ProductDeleteForm productDF) { //TODO delete form
        Optional<Product> productOptional = productRepository.findById(uuid);
        if (productOptional.isPresent()) {
            User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

            eventManager.createProductReportEvent(user.getId(), "User requested deletion of" +
                    " a product with description: \"" + productDF.getDescription() + "\"", uuid);
            //That's all?
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

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public List<Product> getProductsByCategory(UUID uuid) {
        return productRepository.findByCategoryCategoryID(uuid);
    }

    public List<Product> getUnconfirmedSuggestions() {
        return productRepository.findByConfirmedFalse();
    }

    public List<Product> getLatestVersionProduct(UUID uuid) {
        return productRepository.findByConstantProductIdAndDeletedFalse(uuid);
    }


}
