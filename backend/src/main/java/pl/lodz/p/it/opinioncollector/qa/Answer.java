package pl.lodz.p.it.opinioncollector.qa;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.lodz.p.it.opinioncollector.userModule.user.User;

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
    @ManyToOne
    private User author;

}
