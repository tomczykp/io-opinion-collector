package pl.lodz.p.it.opinioncollector.userModule.token;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.lodz.p.it.opinioncollector.userModule.user.User;

import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<Token, Long> {
    Optional<Token> findByToken(String token);

    Optional<Token> findTokenByUserAndType(User user, TokenType type);

    void deleteByToken(String token);

    void deleteAllByUserAndType(User user, TokenType type);

    void deleteAllByUser(User user);
}
