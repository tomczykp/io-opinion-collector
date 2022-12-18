package pl.lodz.p.it.opinioncollector.category;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import pl.lodz.p.it.opinioncollector.exceptions.category.CategoryNotFoundException;
import pl.lodz.p.it.opinioncollector.exceptions.category.FieldNotFoundException;

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

    public Category createCategory(CategoryDTO categoryDTO) throws CategoryNotFoundException {
        Category category = new Category(categoryDTO);
        if(categoryDTO.getParentCategoryID() != null){
            Optional<Category>  parent = categoryRepository.findById(categoryDTO.getParentCategoryID());
            if(parent.isPresent()){
                category.setParentCategory(parent.get());
            }else{
                throw new CategoryNotFoundException();
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

    public Category updateCategory(UUID uuid, UpdateCategoryDTO categoryDTO) throws CategoryNotFoundException {
        Optional<Category> categoryOptional = categoryRepository.findById(uuid);

        if(categoryOptional.isPresent()){
            if(categoryDTO.getParentCategoryID() != null){
                Optional<Category>  parent = categoryRepository.findById(categoryDTO.getParentCategoryID());
                if(parent.isPresent()){
                    categoryOptional.get().setParentCategory(parent.get());
                }else{
                    throw new CategoryNotFoundException(uuid.toString());
                }
            }
            categoryOptional.get().mergeCategory(categoryDTO);
            categoryRepository.save(categoryOptional.get());
            return categoryOptional.get();
        } else {
            throw new CategoryNotFoundException(uuid.toString());
        }
    }

    public boolean deleteCategory(UUID uuid) throws CategoryNotFoundException {
        Optional<Category> category = categoryRepository.findById(uuid);
        if(category.isPresent()){
            categoryRepository.deleteById(uuid);
            return true;
        } else {
            throw new CategoryNotFoundException(uuid.toString());
        }
    }

    public Category addField(UUID categoryId, Field field) throws CategoryNotFoundException {
        Optional<Category> category = categoryRepository.findById(categoryId);
        if(category.isPresent()){
            category.get().getFields().add(field);
            categoryRepository.save(category.get());
            return category.get();
        } else {
            throw new CategoryNotFoundException();
        }
    }

    public void removeField(UUID fieldId) throws FieldNotFoundException {
        Optional<Field> field = fieldRepository.findById(fieldId);
        if(field.isPresent()){
            field.get().getCategories().forEach((category -> {
                category.getFields().remove(field.get());
                categoryRepository.save(category);
            }));
            fieldRepository.deleteById(fieldId);
        } else {
            throw new FieldNotFoundException();
        }
    }

}
