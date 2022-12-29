package pl.lodz.p.it.opinioncollector.category.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.lodz.p.it.opinioncollector.category.model.Category;

import java.util.List;
import java.util.UUID;

@Repository
public interface CategoryRepository extends JpaRepository<Category, UUID> {

    @Query("""
                SELECT c
                FROM Category c
                WHERE c.parentCategory.categoryID = ?1
            """)
    List<Category> findChildrenCategories(UUID categoryId);
}
