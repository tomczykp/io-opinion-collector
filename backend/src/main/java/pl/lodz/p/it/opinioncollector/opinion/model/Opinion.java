package pl.lodz.p.it.opinioncollector.opinion.model;

import java.io.Serializable;
import java.util.LinkedHashSet;
import java.util.Set;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
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
    @GeneratedValue
    private OpinionId id;

    @Column(name = "RATE", nullable = false)
    private int rate;

    @Column(name = "DESCRIPTION", nullable = false)
    private String description;

    @Column(name = "LIKES", nullable = false)
    private int likesCounter;

    // TODO add eager fetching
    @OneToMany(mappedBy = "opinion",
               orphanRemoval = true,
               cascade = { CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.MERGE })
    @Builder.Default
    private Set<Advantage> pros = new LinkedHashSet<>();

    // TODO add eager fetching
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

    @ManyToOne
    private User author;
}
