package pl.lodz.p.it.opinioncollector.category;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.lodz.p.it.opinioncollector.category.model.Category;
import pl.lodz.p.it.opinioncollector.category.model.Field;
import pl.lodz.p.it.opinioncollector.category.model.dto.CategoryDTO;
import pl.lodz.p.it.opinioncollector.category.model.dto.FieldDTO;
import pl.lodz.p.it.opinioncollector.category.model.dto.UpdateCategoryDTO;
import pl.lodz.p.it.opinioncollector.exceptions.category.CategoryNotFoundException;
import pl.lodz.p.it.opinioncollector.exceptions.category.FieldNotFoundException;
import pl.lodz.p.it.opinioncollector.exceptions.category.ParentCategoryNotFoundException;
import pl.lodz.p.it.opinioncollector.exceptions.category.UnsupportedTypeException;

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
    public ResponseEntity<Category> getCategoryById(@PathVariable("uuid")UUID uuid) {
        Category category = null;
        try {
            category = categoryManager.getCategory(uuid);
        } catch (CategoryNotFoundException cnfe) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(category);
    }

    @PostMapping("")
    public ResponseEntity<Category> createCategory(@Valid @RequestBody CategoryDTO categoryDTO) {
        Category category = null;
        try {
            category = categoryManager.createCategory(categoryDTO);
        } catch (UnsupportedTypeException | CategoryNotFoundException cnfe){
            return ResponseEntity.badRequest().build();
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
        } catch (ParentCategoryNotFoundException pcnfe){
            return ResponseEntity.badRequest().build();
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
        } catch (UnsupportedTypeException ute){
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(field);
    }

    @PutMapping("/{uuid}/fields")
    public ResponseEntity<Category> addField(@PathVariable("uuid") UUID uuid, @RequestBody @Valid FieldDTO fieldDTO) throws ClassNotFoundException, NoSuchMethodException, CategoryNotFoundException {
        Category category = null;
        try {
            category = categoryManager.addField(uuid,fieldDTO);

        } catch ( UnsupportedTypeException | CategoryNotFoundException e){
            return ResponseEntity.badRequest().build();
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
