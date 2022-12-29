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
import lombok.ToString;

@Embeddable
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ReactionId implements Serializable {

    @Column
    private OpinionId opinionId = new OpinionId();

    @Column(name = "AUTHOR_ID")
    private UUID authorId;
}
