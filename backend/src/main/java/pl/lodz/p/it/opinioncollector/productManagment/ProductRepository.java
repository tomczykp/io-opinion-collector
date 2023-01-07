package pl.lodz.p.it.opinioncollector.productManagment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID> {

    List<Product> findByCategoryCategoryID(UUID uuid);

    List<Product> findByConfirmedFalse();

    List<Product> findByConstantProductId(UUID constantProductId);

    List<Product> findProductsByConfirmedTrueAndDeletedFalse();

//    @Query("""
//        SELECT p
//        FROM Product p
//        WHERE :value IN (VALUE(p.properties))
//            """)
//    List<Product> getByPropertyValues(String value);

//    @Query("""
//        SELECT p
//        FROM Product p
//        WHERE :value IN (KEY(p.properties))
//            """)
//    List<Product> getByPropertyKey(String key);
}
