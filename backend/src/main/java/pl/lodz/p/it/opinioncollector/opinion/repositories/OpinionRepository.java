package pl.lodz.p.it.opinioncollector.opinion.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.lodz.p.it.opinioncollector.opinion.model.Opinion;
import pl.lodz.p.it.opinioncollector.opinion.model.OpinionId;

public interface OpinionRepository extends JpaRepository<Opinion, OpinionId> {
    List<Opinion> findById_ProductId(UUID productId);

    @Query("""
            SELECT o
            FROM Opinion o
            WHERE o.id.productId = ?1 AND o.id.opinionId = ?2
        """)
    Optional<Opinion> findOne(UUID productId, UUID opinionId);

    void deleteById_ProductIdAndId_OpinionId(UUID productId, UUID opinionId);
}
