package pl.lodz.p.it.opinioncollector.category;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.lodz.p.it.opinioncollector.productManagment.Product;

import java.util.List;
import java.util.UUID;

@Repository
public interface CategoryRepository extends JpaRepository<Category, UUID> {
    List<Category> findByName(String name);

    void deleteByName(String name);

}
