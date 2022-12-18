package pl.lodz.p.it.opinioncollector.category;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.lodz.p.it.opinioncollector.exceptions.category.CategoryNotFoundException;
import pl.lodz.p.it.opinioncollector.exceptions.category.FieldNotFoundException;

import java.util.List;
import java.util.UUID;
@CrossOrigin
@RestController
@RequestMapping("/category")
public class CategoryController {

    private final CategoryManager categoryManager;
    private final FieldManager fieldManager;

    @Autowired
    public CategoryController(CategoryManager categoryManager,
                              FieldManager fieldManager){
        this.categoryManager = categoryManager;
        this.fieldManager = fieldManager;
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
    public ResponseEntity<Category> createCategory(@Valid @RequestBody CategoryDTO categoryDTO) {
        Category category = null;
        try {
            category = categoryManager.createCategory(categoryDTO);
        } catch (CategoryNotFoundException cnfe){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(category);
    }

    @PutMapping("/{uuid}")
    public ResponseEntity<Category> updateCategory(@PathVariable ("uuid") UUID uuid,
                                   @Valid @RequestBody UpdateCategoryDTO categoryDTO) throws CategoryNotFoundException{
        Category category = null;
        try{
            category = categoryManager.updateCategory(uuid, categoryDTO);
        } catch (CategoryNotFoundException cnfe){
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(category);
    }

    //@DeleteMapping("/{uuid}")
    public ResponseEntity<Category> deleteCategory(@PathVariable("uuid") UUID uuid) throws CategoryNotFoundException {
        try {
            if(categoryManager.deleteCategory(uuid)){
                return ResponseEntity.ok().build();
            }
        } catch (CategoryNotFoundException cnfe) {
            return ResponseEntity.notFound().build();
        }
        return null;
    }

    @PutMapping("/fields/{uuid}")
    public ResponseEntity<Field> updateField(@PathVariable ("uuid") UUID uuid,
                                             @Valid @RequestBody FieldDTO fieldDTO) throws FieldNotFoundException {
        Field field = null;
        try {
            field = fieldManager.updateField(uuid, fieldDTO);
        } catch (FieldNotFoundException fnfe) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(field);
    }

    @PutMapping("/{uuid}/fields")
    public ResponseEntity<Category> addField(@PathVariable("uuid") UUID uuid, @RequestBody FieldDTO fieldDTO) throws ClassNotFoundException, NoSuchMethodException, CategoryNotFoundException {
        Field field = new Field(fieldDTO);
        Category category = null;
        try {
            category = categoryManager.addField(uuid,field);

        } catch (CategoryNotFoundException e){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(category);
    }

    @DeleteMapping("/fields/{uuid}")
    public void removeField(@PathVariable("uuid") UUID uuid) {
        try {
            categoryManager.removeField(uuid);
        } catch (FieldNotFoundException ignored){

        }
    }



}
