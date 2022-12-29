package pl.lodz.p.it.opinioncollector.productManagment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import pl.lodz.p.it.opinioncollector.eventHandling.IProductEventManager;
import pl.lodz.p.it.opinioncollector.userModule.user.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ProductManager implements IProductManager {
    private final ProductRepository productRepository;
    private final IProductEventManager eventManager;

    @Autowired
    public ProductManager(ProductRepository productRepository, IProductEventManager eventManager) {
        this.productRepository = productRepository;
        this.eventManager = eventManager;
    }


    public Product createProduct(ProductDTO productDTO) {
        Product product = new Product(productDTO);
        productRepository.save(product);
        return product;
    }

    public Product createSuggestion(ProductDTO productDTO) {
        Product product = new Product(productDTO);
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        product.setConfirmed(false);
        eventManager.createProductReportEvent(user.getId(), "New product suggestion with name: \""
                        + product.getName() + "\" and description: \"" + product.getDescription() + "\"",
                product.getProductId());
        productRepository.save(product);
        return product;
    }

    public Product getProduct(UUID uuid) {
        Optional<Product> product = productRepository.findById(uuid);
        return product.orElse(null);
    }

    public Product updateProduct(UUID uuid, ProductDTO productDTO) {
        Optional<Product> productOptional = productRepository.findById(uuid);
        if (productOptional.isPresent()) {
            User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

            productOptional.get().mergeProduct(productDTO); //FIXME
            eventManager.createProductReportEvent(user.getId(),
                    "some words about update", productOptional.get().getProductId());
            productRepository.save(productOptional.get()); //FIXME New object with reference to the old
            return productOptional.get();
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

            eventManager.createProductReportEvent(user.getId(),"User requested deletion of" +
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
        return productRepository.findByCategoryId(uuid);
    }

    public List<Product> getUnconfirmedSuggestions() {
        return productRepository.findByConfirmedFalse();
    }


}
