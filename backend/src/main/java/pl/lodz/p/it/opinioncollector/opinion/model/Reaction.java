package pl.lodz.p.it.opinioncollector.opinion.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinColumns;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.lodz.p.it.opinioncollector.userModule.user.User;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Reaction {
    @EmbeddedId
    @EqualsAndHashCode.Include
    private ReactionId id = new ReactionId();

    @ManyToOne(optional = false)
    @JsonIgnore
    @MapsId("authorId")
    private User author;

    @JsonIgnore
    @ManyToOne(optional = false)
    @MapsId("opinionId")
    @JoinColumns({
        @JoinColumn(name = "OPINION_ID", referencedColumnName = "opinion_id"),
        @JoinColumn(name = "PRODUCT_ID", referencedColumnName = "product_id")
    })
    private Opinion opinion;

    private boolean positive;

    public Reaction(User author, Opinion opinion, boolean positive) {
        this.id = new ReactionId(opinion.getId(), author.getId());
        this.author = author;
        this.opinion = opinion;
        this.positive = positive;
    }
}
