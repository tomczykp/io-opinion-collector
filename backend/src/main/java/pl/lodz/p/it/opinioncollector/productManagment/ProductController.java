package pl.lodz.p.it.opinioncollector.productManagment;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.lodz.p.it.opinioncollector.exceptions.category.CategoryNotFoundException;
import pl.lodz.p.it.opinioncollector.exceptions.products.ProductCannotBeEditedException;
import pl.lodz.p.it.opinioncollector.exceptions.products.ProductNotFoundException;

import java.util.List;
import java.util.UUID;

@CrossOrigin
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
    public ResponseEntity<Product> getProductById(@PathVariable("uuid") UUID uuid) throws ProductNotFoundException {
        Product product = productManager.getProduct(uuid);
        if (product == null) {
            throw new ProductNotFoundException();
        }
        return ResponseEntity.ok(product);
    }

    @GetMapping("/category/{uuid}")
    public List<Product> getProductsByCategory(@PathVariable("uuid") UUID uuid) {
        return productManager.getProductsByCategory(uuid);
    }

    @GetMapping("/constant/{uuid}")
    public List<Product> getProductsByConstantId(@PathVariable("uuid") UUID uuid) {
        return productManager.getProductsByConstantId(uuid);
    }


    @GetMapping("/unconfirmed")
    public List<Product> getUnconfirmedSuggestions() {
        return productManager.getUnconfirmedSuggestions();
    }

    //PostMapping

    @PostMapping("")
    public ResponseEntity<Product> createProduct(@Valid @RequestBody ProductDTO productDTO) {
        Product product = productManager.createProduct(productDTO);
        return ResponseEntity.ok(product);
    }

    @PostMapping("/suggestion")
    public ResponseEntity<Product> createSuggestion(@Valid @RequestBody ProductDTO productDTO) throws CategoryNotFoundException {
        Product product = productManager.createSuggestion(productDTO);
        return ResponseEntity.ok(product);
    }

    //PutMapping
    @PutMapping("/{uuid}")
    public ResponseEntity<Product> updateProduct(@PathVariable("uuid") UUID uuid,
                                                 @Valid @RequestBody ProductDTO productDTO) throws ProductNotFoundException, ProductCannotBeEditedException {
        Product product = productManager.updateProduct(uuid, productDTO);
        if (product == null) {
            throw new ProductNotFoundException();
        }
        return ResponseEntity.ok(product);
    }

    @PutMapping("/{uuid}/confirm")
    public ResponseEntity<?> confirmProduct(@PathVariable("uuid") UUID uuid) throws ProductNotFoundException {
        if (!productManager.confirmProduct(uuid)) {
            throw new ProductNotFoundException();
        }
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{uuid}/unconfirm")
    public ResponseEntity<?> unconfirmProduct(@PathVariable("uuid") UUID uuid) throws ProductNotFoundException {
        if (!productManager.unconfirmProduct(uuid)) {
            throw new ProductNotFoundException();
        }
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{uuid}/delete")
    public ResponseEntity<?> makeDeleteFormProduct(@PathVariable("uuid") UUID uuid,
                                                   @Valid @RequestBody ProductDeleteForm productDF) throws ProductNotFoundException {
        if (!productManager.makeDeleteFormProduct(uuid, productDF)) {
            throw new ProductNotFoundException();
        }
        return ResponseEntity.noContent().build();
    }

    //DeleteMapping
    @DeleteMapping("/{uuid}")
    public ResponseEntity<Product> deleteProduct(@PathVariable("uuid") UUID uuid) throws ProductNotFoundException {
        if (productManager.deleteProduct(uuid)) {
            return ResponseEntity.ok().build();
        }
        throw new ProductNotFoundException();
    }
}
