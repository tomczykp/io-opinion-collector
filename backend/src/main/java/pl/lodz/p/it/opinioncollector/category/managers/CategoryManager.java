package pl.lodz.p.it.opinioncollector.category.managers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import pl.lodz.p.it.opinioncollector.category.model.Category;
import pl.lodz.p.it.opinioncollector.category.model.Field;
import pl.lodz.p.it.opinioncollector.category.model.dto.CategoryDTO;
import pl.lodz.p.it.opinioncollector.category.model.dto.FieldDTO;
import pl.lodz.p.it.opinioncollector.category.model.dto.UpdateCategoryDTO;
import pl.lodz.p.it.opinioncollector.category.repositories.CategoryRepository;
import pl.lodz.p.it.opinioncollector.category.repositories.FieldRepository;
import pl.lodz.p.it.opinioncollector.exceptions.category.*;

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
                           FieldRepository fieldRepository) {
        this.categoryRepository = categoryRepository;
        this.fieldRepository = fieldRepository;
    }

    public Category createCategory(CategoryDTO categoryDTO) throws ParentCategoryNotFoundException, UnsupportedTypeException {
        Category category = new Category(categoryDTO);
        if (categoryDTO.getParentCategoryID() != null) {
            Optional<Category> parent = categoryRepository.findById(categoryDTO.getParentCategoryID());
            if (parent.isPresent()) {
                category.setParentCategory(parent.get());
            } else {
                throw new ParentCategoryNotFoundException();
            }
        }
        categoryRepository.save(category);
        return category;
    }

    public Category getCategory(UUID uuid) throws CategoryNotFoundException {
        Optional<Category> category = categoryRepository.findById(uuid);
        if (category.isPresent()) {
            return category.get();
        }
        throw new CategoryNotFoundException(uuid.toString());
    }

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public List<Category> getCategories(Predicate<Category> Predicate) {
        List<Category> allCategories = categoryRepository.findAll();
        List<Category> result = new ArrayList<Category>();
        for (Category c : allCategories) {
            if (Predicate.test(c)) {
                result.add(c);
            }
        }
        return result;
    }

    public Category updateCategory(UUID uuid, UpdateCategoryDTO categoryDTO) throws CategoryNotFoundException, ParentCategoryNotFoundException {
        Optional<Category> categoryOptional = categoryRepository.findById(uuid);

        if (categoryOptional.isPresent()) {
            String parentCategoryString = categoryDTO.getParentCategoryID();
            Category category = categoryOptional.get();
            if (parentCategoryString != null && !parentCategoryString.isBlank()) {
                UUID parentUUID = null;
                try {
                    parentUUID = UUID.fromString(parentCategoryString);
                } catch (IllegalArgumentException e) {
                    throw new ParentCategoryNotFoundException();
                }

                if (category.getParentCategory() == null
                        || !parentUUID.equals(
                        category.getParentCategory().getCategoryID())) {
                    if (parentUUID.equals(category.getCategoryID())) {
                        throw new ParentCategoryNotFoundException(); //możnaby dać inny wyjątek, ale i tak by był zmapowany do 400 BAD_REQUEST
                    }

                    Optional<Category> parent = categoryRepository.findById(parentUUID);
                    if (parent.isPresent()) {
                        category.setParentCategory(parent.get());
                    } else {
                        throw new ParentCategoryNotFoundException();
                    }
                }
            } else {
                category.setParentCategory(null);
            }

            String newName = categoryDTO.getName();
            if (!newName.equals(category.getName())) {
                category.setName(newName);
            }

            categoryRepository.save(category);
            return categoryOptional.get();
        } else {
            throw new CategoryNotFoundException(uuid.toString());
        }
    }

    public boolean deleteCategory(UUID uuid) throws CategoryNotFoundException, ConstraintException {
        Optional<Category> category = categoryRepository.findById(uuid);
        if (category.isPresent()) {
            List<Category> children = categoryRepository.findChildrenCategories(uuid);
            if (!children.isEmpty()) {
                for (Category c : children) {
                    c.setParentCategory(category.get().getParentCategory());
                    categoryRepository.save(c);
                }
            }
            try {
                categoryRepository.deleteById(uuid);
            } catch (DataIntegrityViolationException e) {
                throw new ConstraintException();
            }
            return true;
        } else {
            throw new CategoryNotFoundException(uuid.toString());
        }
    }

    public synchronized Field addField(UUID categoryId, FieldDTO fieldDTO) throws CategoryNotFoundException, UnsupportedTypeException {
        Optional<Category> optionalCategory = categoryRepository.findById(categoryId);
        if (optionalCategory.isPresent()) {
            Category category = optionalCategory.get();
            Field field = new Field(fieldDTO);
            category.getFields().add(field);
            categoryRepository.save(category);
            return field;
        } else {
            throw new CategoryNotFoundException();
        }
    }

    public void removeField(UUID fieldId) throws FieldNotFoundException {
        Optional<Field> field = fieldRepository.findById(fieldId);
        if (field.isPresent()) {
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
