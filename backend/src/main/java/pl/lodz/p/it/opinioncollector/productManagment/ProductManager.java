package pl.lodz.p.it.opinioncollector.productManagment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.lodz.p.it.opinioncollector.eventHandling.IProductEventManager;

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
        product.setConfirmed(false);        //FIXME temp random UUID
        eventManager.createProductReportEvent(UUID.randomUUID(), "New product suggestion with name: \""
                        + product.getName() + "\" and description: \"" + product.getDescription() + "\"",
                product.getProductId());
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

    //TODO
    // If we want to make history of changes then we need some unique name or second UUID, the first must stay the same
    public Product updateProduct(UUID uuid, ProductDTO productDTO) {
        Optional<Product> productOptional = productRepository.findById(uuid);
        if (productOptional.isPresent()) {
            productOptional.get().mergeProduct(productDTO);
//            eventManager.createProductReportEvent(USER_ID, "some words about update", productDTO.getProductId())
            productRepository.save(productOptional.get()); //FIXME New object and send suggestion to events?
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
