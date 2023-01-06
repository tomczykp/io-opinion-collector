package pl.lodz.p.it.opinioncollector.qa;


import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID questionId;

    private String content;
    private LocalDateTime date;
    private UUID productId;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String author;

}
