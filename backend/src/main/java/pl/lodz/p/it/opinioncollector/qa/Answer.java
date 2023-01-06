package pl.lodz.p.it.opinioncollector.qa;


import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Answer {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID answerId;
    private String content;
    private LocalDateTime date;
    private UUID questionId;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String author;

}
