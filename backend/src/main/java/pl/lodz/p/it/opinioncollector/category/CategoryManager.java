package pl.lodz.p.it.opinioncollector.category;


import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;

public class CategoryManager {

    private List<Category> categories;

    public CategoryManager()
    {
        categories = new ArrayList<Category>();
    }

    public Category createCategory(String name, List<Field> fields)
    {
        Category category = new Category(UUID.randomUUID(), name, fields);
        categories.add(category);
        return category;
    }

    public Category getCategory(UUID categoryID)
    {
        for(Category c: categories){
            if(c.getCategoryID() == categoryID)
                return c;
        }
        return null;
    }

    public List<Category> getCategories(Predicate<Category> Predicate)
    {
        List<Category> result = new ArrayList<Category> ();
        for(Category c: categories){
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

    public boolean deleteCategory(String name)
    {
        for(Category c: categories){
            if(c.getName() == name){
                return categories.remove(c);
            }
        }
        return false;
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
