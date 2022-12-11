package pl.lodz.p.it.opinioncollector.category;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;

@Service
public class CategoryManager {

    private CategoryRepository categories;
    @Autowired
    public CategoryManager(CategoryRepository categoryRepository)
    {
        categories = categoryRepository;
    }

    public Category createCategory(String name, List<Field> fields)
    {
        Category category = new Category(UUID.randomUUID(), name, fields);
        categories.save(category);
        return category;
    }

    public Category getCategory(UUID categoryID)
    {
        return categories.findById(categoryID).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST));
    }

    public List<Category> getCategories(Predicate<Category> Predicate)
    {
        List<Category> allCategories  = categories.findAll();
        List<Category> result = new ArrayList<Category> ();
        for(Category c: allCategories){
            if(Predicate.test(c)) {
                result.add(c);
            }
        }
        return result;
    }

    public Category modifyCategory(Category modifiedCategory, String name, List<Field> fields)
    {
        modifiedCategory.setName(name);
        modifiedCategory.setFields(fields);
        return modifiedCategory;
    }

    public void deleteCategory(String name)
    {
        categories.deleteByName(name);
    }

    public Field getField(UUID categoryID, String filedName)
    {
        Category c = getCategory(categoryID);
        for(Field f: c.getFields()){
            if(f.getName()==filedName){
                return f;
            }
        }
        return null;
    }
}
