package pl.lodz.p.it.opinioncollector.userModule.token;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.lodz.p.it.opinioncollector.userModule.user.User;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Token {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String token;

    @Enumerated(EnumType.STRING)
    private TokenType type;

    private Instant createdAt;
    @OneToOne(fetch = FetchType.LAZY)
    private User user;
}
