package pl.lodz.p.it.opinioncollector.opinion;

import java.util.UUID;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
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
public class Advantage {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, name = "VALUE")
    private String value;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JsonIgnore
    private Opinion opinion;

    public Advantage(String value, Opinion opinion) {
        this.value = value;
        this.opinion = opinion;
    }
}
