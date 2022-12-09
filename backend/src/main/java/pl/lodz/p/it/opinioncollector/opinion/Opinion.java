package pl.lodz.p.it.opinioncollector.opinion;

import java.io.Serializable;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.lodz.p.it.opinioncollector.productManagment.Product;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Opinion implements Serializable {

    @EmbeddedId
    private OpinionId id;

    @Column(name = "RATE", nullable = false)
    private int rate;

    @Column(name = "DESCRIPTION", nullable = false)
    private String description;

    @Column(name = "LIKES", nullable = false)
    private int likesCounter;

    // TODO add pros and cons

    @MapsId("PRODUCT_ID")
    @ManyToOne(optional = false)
    @JoinColumn(name = "PRODUCT_ID", nullable = false)
    private Product product;

    // TODO uncomment when User is marked with @Entity
    // @ManyToOne
    // private User author;
}
