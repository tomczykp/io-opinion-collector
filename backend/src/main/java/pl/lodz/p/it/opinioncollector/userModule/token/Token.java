package pl.lodz.p.it.opinioncollector.userModule.token;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cascade;
import pl.lodz.p.it.opinioncollector.userModule.user.User;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Token {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String token;
    @Enumerated(EnumType.STRING)
    private TokenType type;
    private Instant expiresAt;
    @OneToOne(fetch = FetchType.LAZY)
    private User user;

    public Token(String token, TokenType type, User user) {
        this.token = token;
        this.type = type;
        this.user = user;
        switch (type) {
            case REFRESH_TOKEN, VERIFICATION_TOKEN -> this.expiresAt = Instant.now().plus(7, ChronoUnit.DAYS);
            case PASSWORD_RESET_TOKEN -> this.expiresAt = Instant.now().plus(1, ChronoUnit.DAYS);
            case DELETION_TOKEN -> this.expiresAt = Instant.now().plus(30, ChronoUnit.MINUTES);
        }
    }
}
