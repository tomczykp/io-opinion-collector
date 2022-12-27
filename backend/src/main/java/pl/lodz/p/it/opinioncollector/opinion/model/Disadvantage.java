package pl.lodz.p.it.opinioncollector.opinion.model;

import java.io.Serializable;
import java.util.UUID;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Disadvantage implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, name = "VALUE")
    private String value;

    @ManyToOne(optional = false)
    @JsonIgnore
    private Opinion opinion;

    public Disadvantage(String value, Opinion opinion) {
        this.value = value;
        this.opinion = opinion;
    }
}
