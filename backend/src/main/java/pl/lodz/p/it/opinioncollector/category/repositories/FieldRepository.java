package pl.lodz.p.it.opinioncollector.category.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.lodz.p.it.opinioncollector.category.model.Field;

import java.util.List;
import java.util.UUID;

public interface FieldRepository extends JpaRepository<Field, UUID> {
    List<Field> findByName(String name);

    void deleteByName(String name);
}
