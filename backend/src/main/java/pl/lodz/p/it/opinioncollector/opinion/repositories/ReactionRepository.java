package pl.lodz.p.it.opinioncollector.opinion.repositories;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.lodz.p.it.opinioncollector.opinion.model.OpinionId;
import pl.lodz.p.it.opinioncollector.opinion.model.Reaction;
import pl.lodz.p.it.opinioncollector.opinion.model.ReactionId;

public interface ReactionRepository extends JpaRepository<Reaction, ReactionId> {

    @Query("""
            SELECT COALESCE(
                SUM(CASE
                        WHEN r.positive = TRUE THEN 1
                        ELSE -1
                    END), 0)
            FROM Reaction r
            WHERE r.id.opinionId = :opinionId
        """)
    int calculateLikesCounter(OpinionId opinionId);

    @Query("""
        SELECT r.positive
        FROM Reaction r
        WHERE r.id.opinionId.productId = :productId
              AND r.id.opinionId.opinionId = :opinionId
              AND r.id.authorId = :authorId
        """)
    Boolean findReaction(UUID productId, UUID opinionId, UUID authorId);
}
