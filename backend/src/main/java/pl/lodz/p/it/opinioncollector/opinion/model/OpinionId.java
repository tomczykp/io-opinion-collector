package pl.lodz.p.it.opinioncollector.opinion.model;

import java.io.Serializable;
import java.util.UUID;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Composite primary key for opinions.
 */
@Embeddable
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class OpinionId implements Serializable {
    @Column(name = "PRODUCT_ID", nullable = false)
    private UUID productId;

    @Column(name = "OPINION_ID", nullable = false)
    private UUID opinionId;
}
