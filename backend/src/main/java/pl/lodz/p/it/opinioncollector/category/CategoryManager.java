package pl.lodz.p.it.opinioncollector.category;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Predicate;

@Service
public class CategoryManager {

    private final CategoryRepository categoryRepository;
    private final FieldRepository fieldRepository;

    @Autowired
    public CategoryManager(CategoryRepository categoryRepository,
                           FieldRepository fieldRepository)
    {
        this.categoryRepository = categoryRepository;
        this.fieldRepository = fieldRepository;
    }

    public Category createCategory(CategoryDTO categoryDTO)
    {
        Category category = new Category(categoryDTO);
        if(categoryDTO.getParentCategoryID() != null){
            Optional<Category>  parent = categoryRepository.findById(categoryDTO.getParentCategoryID());
            if(parent.isPresent()){
                category.setParentCategory(parent.get());
            }else{
                category.setParentCategory(null);
            }
        }
        categoryRepository.save(category);
        return category;
    }

    public Category getCategory(UUID uuid)
    {
        Optional<Category> category = categoryRepository.findById(uuid);
        if(category.isPresent()){
            return category.get();
        }
        return null;
    }

    public List<Category> getAllCategories(){
        return categoryRepository.findAll();
    }

    public List<Category> getCategories(Predicate<Category> Predicate)
    {
        List<Category> allCategories  = categoryRepository.findAll();
        List<Category> result = new ArrayList<Category> ();
        for(Category c: allCategories){
            if(Predicate.test(c)) {
                result.add(c);
            }
        }
        return result;
    }

    public Category updateCategory(UUID uuid, UpdateCategoryDTO categoryDTO)
    {
        Optional<Category> categoryOptional = categoryRepository.findById(uuid);

        if(categoryOptional.isPresent()){
            if(categoryDTO.getParentCategoryID() != null){
                Optional<Category>  parent = categoryRepository.findById(categoryDTO.getParentCategoryID());
                if(parent.isPresent()){
                    categoryOptional.get().setParentCategory(parent.get());
                }else{
                    categoryOptional.get().setParentCategory(null);
                }
            }
            categoryOptional.get().mergeCategory(categoryDTO);
            categoryRepository.save(categoryOptional.get());
            return categoryOptional.get();
        }
        return null;
    }

    public boolean deleteCategory(UUID uuid)
    {
        Optional<Category> category = categoryRepository.findById(uuid);
        if(category.isPresent()){
            categoryRepository.deleteById(uuid);
            return true;
        }
        return false;
    }

    public Category addField(UUID categoryId, Field field){
        Optional<Category> category = categoryRepository.findById(categoryId);
        if(category.isPresent()){
            category.get().getFields().add(field);
            categoryRepository.save(category.get());
            return category.get();
        }
        return null;
    }

    public void removeField(UUID fieldId){
        Optional<Field> field = fieldRepository.findById(fieldId);
        if(field.isPresent()){
            field.get().getCategories().forEach((category -> {
                category.getFields().remove(field.get());
                categoryRepository.save(category);
            }));
        }
        fieldRepository.deleteById(fieldId);
    }

}