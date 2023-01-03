package pl.lodz.p.it.opinioncollector.opinion.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Formula;
import pl.lodz.p.it.opinioncollector.productManagment.Product;
import pl.lodz.p.it.opinioncollector.userModule.user.User;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Opinion implements Serializable {

    @EmbeddedId
    private OpinionId id;

    @Column(nullable = false,
            insertable = false,
            updatable = false,
            columnDefinition = "TIMESTAMP DEFAULT NOW()")
    private LocalDateTime createdAt;

    @Column(name = "RATE", nullable = false)
    private int rate;

    @Column(name = "DESCRIPTION", nullable = false)
    private String description;

    @Formula("""
        (SELECT COALESCE(
            SUM(CASE
                    WHEN R.POSITIVE THEN 1
                    ELSE -1
                END), 0)
        FROM REACTION R
        WHERE R.PRODUCT_ID = PRODUCT_ID
              AND R.OPINION_ID = OPINION_ID)
        """)
    private int likesCounter;

    @OneToMany(mappedBy = "opinion",
               orphanRemoval = true,
               cascade = { CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.MERGE })
    @Builder.Default
    private Set<Advantage> pros = new LinkedHashSet<>();

    @OneToMany(orphanRemoval = true,
               cascade = { CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.MERGE },
               mappedBy = "opinion")
    @Builder.Default
    private Set<Disadvantage> cons = new LinkedHashSet<>();

    @MapsId("PRODUCT_ID")
    @ManyToOne(optional = false)
    @JoinColumn(name = "PRODUCT_ID", nullable = false)
    @JsonIgnore
    private Product product;

    @ManyToOne(optional = false)
    private User author;

    @JsonIgnore
    @OneToMany(orphanRemoval = true,
               cascade = CascadeType.ALL,
               mappedBy = "opinion",
               fetch = FetchType.LAZY)
    private Set<Reaction> reactions;
}
