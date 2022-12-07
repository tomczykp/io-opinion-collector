package pl.lodz.p.it.opinioncollector.productManagment;

import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import pl.lodz.p.it.opinioncollector.productManagment.exceptions.ProductNotFoundException;

import java.util.List;
import java.util.UUID;

@RestController()
@RequestMapping("/products")
public class ProductController {
    private final ProductManager productManager;

    @Autowired
    public ProductController(ProductManager productManager) {
        this.productManager = productManager;
    }

    //GetMapping

    @GetMapping("")
    public List<Product> getAllProducts() {
        return productManager.getAllProducts();
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<Product> getProductById(@PathVariable("uuid") UUID uuid) {
        Product product = productManager.getProduct(uuid);
        if (product == null) {
            throw new ProductNotFoundException(uuid.toString());
        }
        return ResponseEntity.ok(product);
    }

    @GetMapping("/category/{uuid}")
    public List<Product> getProductsByCategory(@PathVariable("uuid") UUID uuid) {
        return productManager.getProductsByCategory(uuid);
    }

    @GetMapping("/unconfirmed")
    public List<Product> getUnconfirmedSuggestions() {
        return productManager.getUnconfirmedSuggestions();
    }

    //PutMapping

    @PutMapping("")
    public ResponseEntity<Product> createProduct(@Valid @RequestBody ProductDTO productDTO) throws MethodArgumentNotValidException {
        Product product = productManager.createProduct(productDTO);
        return ResponseEntity.ok(product);
    }

    @PutMapping("/suggestion")
    public ResponseEntity<Product> createSuggestion(@Valid @RequestBody ProductDTO productDTO) throws MethodArgumentNotValidException {
        Product product = productManager.createSuggestion(productDTO);
        return ResponseEntity.ok(product);
    }

    //PostMapping

    @PostMapping("/{uuid}")
    public ResponseEntity<Product> updateProduct(@PathVariable("uuid") UUID uuid,
                                                 @Valid @RequestBody ProductDTO productDTO) throws MethodArgumentNotValidException {
        Product product = productManager.updateProduct(uuid, productDTO);
        if (product == null) {
            throw new ProductNotFoundException(uuid.toString());
        }
        return ResponseEntity.ok(product);
    }

    //DeleteMapping

    @DeleteMapping("/{uuid}")
    public ResponseEntity<Product> deleteProduct(@PathVariable("uuid") UUID uuid) {
        if (productManager.deleteProduct(uuid)) {
            return ResponseEntity.ok().build();
        }
        throw new ProductNotFoundException(uuid.toString());
    }
}