package pl.lodz.p.it.opinioncollector.category;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.lodz.p.it.opinioncollector.category.managers.CategoryManager;
import pl.lodz.p.it.opinioncollector.category.managers.FieldManager;
import pl.lodz.p.it.opinioncollector.category.model.Category;
import pl.lodz.p.it.opinioncollector.category.model.Field;
import pl.lodz.p.it.opinioncollector.category.model.dto.CategoryDTO;
import pl.lodz.p.it.opinioncollector.category.model.dto.FieldDTO;
import pl.lodz.p.it.opinioncollector.category.model.dto.UpdateCategoryDTO;
import pl.lodz.p.it.opinioncollector.exceptions.category.*;

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
                              FieldManager fieldManager) {
        this.categoryManager = categoryManager;
        this.fieldManager = fieldManager;
    }

    @GetMapping("")
    public List<Category> getAllCategories() {
        return categoryManager.getAllCategories();
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<Category> getCategoryById(@PathVariable("uuid") UUID uuid) throws CategoryNotFoundException {
        Category category = categoryManager.getCategory(uuid);
        return ResponseEntity.ok(category);
    }

    @PostMapping("")
    public ResponseEntity<Category> createCategory(@Valid @RequestBody CategoryDTO categoryDTO) throws ParentCategoryNotFoundException, UnsupportedTypeException {
        Category category = categoryManager.createCategory(categoryDTO);
        return ResponseEntity.ok(category);
    }

    @PutMapping("/{uuid}")
    public ResponseEntity<Category> updateCategory(@PathVariable("uuid") UUID uuid,
                                                   @Valid @RequestBody UpdateCategoryDTO categoryDTO) throws CategoryNotFoundException, ParentCategoryNotFoundException {
        Category category = categoryManager.updateCategory(uuid, categoryDTO);
        return ResponseEntity.ok(category);
    }

    @DeleteMapping("/{uuid}")
    public ResponseEntity<Category> deleteCategory(@PathVariable("uuid") UUID uuid) throws CategoryNotFoundException, ConstraintException {
        if (categoryManager.deleteCategory(uuid)) {
            return ResponseEntity.ok().build();
        }
        return null;
    }

    @PutMapping("/fields/{uuid}")
    public ResponseEntity<Field> updateField(@PathVariable("uuid") UUID uuid,
                                             @Valid @RequestBody FieldDTO fieldDTO) throws FieldNotFoundException, UnsupportedTypeException {
        Field field = fieldManager.updateField(uuid, fieldDTO);
        return ResponseEntity.ok(field);
    }

    @PutMapping("/{uuid}/fields")
    public ResponseEntity<Category> addField(@PathVariable("uuid") UUID uuid, @RequestBody @Valid FieldDTO fieldDTO) throws CategoryNotFoundException, UnsupportedTypeException {
        Category category = categoryManager.addField(uuid, fieldDTO);
        return ResponseEntity.ok(category);
    }

    @DeleteMapping("/fields/{uuid}")
    public void removeField(@PathVariable("uuid") UUID uuid) throws FieldNotFoundException {
        categoryManager.removeField(uuid);
    }

}
