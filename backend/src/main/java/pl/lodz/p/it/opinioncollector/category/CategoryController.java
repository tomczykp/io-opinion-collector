package pl.lodz.p.it.opinioncollector.category;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.lodz.p.it.opinioncollector.exceptions.category.CategoryNotFoundException;

import java.util.List;
import java.util.UUID;
@CrossOrigin
@RestController
@RequestMapping("/category")
public class CategoryController {

    private final CategoryManager categoryManager;

    @Autowired
    public CategoryController(CategoryManager categoryManager){
        this.categoryManager = categoryManager;
    }

    @GetMapping("")
    public List<Category> getAllCategories(){
        return categoryManager.getAllCategories();
    }

    @GetMapping("/{uuid}")

    public ResponseEntity<Category> getCategoryById(@PathVariable("uuid")UUID uuid) throws CategoryNotFoundException {
        Category category = categoryManager.getCategory(uuid);
        if(category == null){
            throw new CategoryNotFoundException(uuid.toString());
        }
        return ResponseEntity.ok(category);
    }

    @PostMapping("")
    public ResponseEntity<Category> createCategory(@Valid @RequestBody CategoryDTO categoryDTO){
        Category category = categoryManager.createCategory(categoryDTO);
        return  ResponseEntity.ok(category);

    

    @PutMapping("/{uuid}")
    public ResponseEntity<Category> updateCategory(@PathVariable ("uuid") UUID uuid,
                                   @Valid @RequestBody CategoryDTO categoryDTO) throws CategoryNotFoundException{
        Category category = categoryManager.updateCategory(uuid, categoryDTO);
        if(category == null){
            throw new CategoryNotFoundException(uuid.toString());
        }
        return ResponseEntity.ok(category);
    }

    @DeleteMapping("/{uuid}")
    public ResponseEntity<Category> deleteCategory(@PathVariable("uuid") UUID uuid) throws CategoryNotFoundException {
        if(categoryManager.deleteCategory(uuid)){
            return ResponseEntity.ok().build();
        }
        throw new CategoryNotFoundException(uuid.toString());
    }

    @PutMapping("/{uuid}/fields")
    public ResponseEntity<Category> addField(@PathVariable("uuid") UUID uuid, @RequestBody FieldDTO fieldDTO) throws ClassNotFoundException, NoSuchMethodException, CategoryNotFoundException {
        Field field = new Field(fieldDTO);
        Category category = categoryManager.addField(uuid,field);
        if(category != null){
            return ResponseEntity.ok(category);
        }
        throw new CategoryNotFoundException(uuid.toString());
    }

}