package pl.lodz.p.it.opinioncollector.category;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/category")
public class CategoryController {

    private CategoryManager categoryManager;

    @Autowired
    public CategoryController(CategoryManager categoryManager){
        this.categoryManager=categoryManager;
    }
    @GetMapping("/{uuid}")
    public Category getCategory(@PathVariable("uuid")UUID uuid){
        return  categoryManager.getCategory(uuid);
    }


    @PutMapping("/category")
    public Category createCategory(@PathVariable String name, @PathVariable List<Field> fields ){
        return  categoryManager.createCategory(name, fields);
    }

    @PostMapping("")
    public Category modifyCategory(Category category, String name, List<Field> fields){
        return  categoryManager.modifyCategory(category, name, fields);
    }

    @DeleteMapping("/{name}")
    public void deleteCategory(@PathVariable("name") String name){
        categoryManager.deleteCategory(name);
    }
}