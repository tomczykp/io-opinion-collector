package pl.lodz.p.it.opinioncollector.productManagment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController()
public class ProductController {
    ProductManager productManager;

    @Autowired
    public ProductController(ProductManager productManager) {
        this.productManager = productManager;
    }

    //GetMapping

    @GetMapping("/products")
    public List<Product> getAllProducts() {
        return productManager.getAllProducts();
    }

    @GetMapping("/products/{uuid}")
    public ResponseEntity<Product> getProductById(@PathVariable("uuid") UUID uuid) {
        Optional<Product> product = productManager.getProduct(uuid);
        if (product.isPresent()) {
            return ResponseEntity.ok(product.get());
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/products/category/{uuid}")
    public List<Product> getProductsByCategory(@PathVariable("uuid") UUID uuid) {
        return productManager.getProductsByCategory(uuid);
    }

    @GetMapping("/products/unconfirmed")
    public List<Product> getUnconfirmedSuggestions() {
        return productManager.getUnconfirmedSuggestions();
    }

    //PutMapping

    @PutMapping("/products")
    public Product createProduct() {
        //TODO DTO?
        return null;
    }

    @PutMapping("/products/suggestion")
    public Product createSuggestion() {
        //TODO DTO?
        return null;
    }

    //PostMapping

    @PostMapping("/products/{product}")
    public Product updateProduct() {
        //TODO
        return null;
    }

}
