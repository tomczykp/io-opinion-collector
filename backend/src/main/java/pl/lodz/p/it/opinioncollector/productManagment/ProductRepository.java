package pl.lodz.p.it.opinioncollector.productManagment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID> {

    List<Product> findByCategoryCategoryID(UUID uuid);

    List<Product> findByConfirmedFalse();

    List<Product> findByConstantProductIdAndDeletedFalse(UUID constantProductId);
}
