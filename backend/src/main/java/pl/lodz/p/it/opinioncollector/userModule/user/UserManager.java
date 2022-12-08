package pl.lodz.p.it.opinioncollector.userModule.user;


import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import pl.lodz.p.it.opinioncollector.userModule.auth.*;
import pl.lodz.p.it.opinioncollector.userModule.token.Token;
import pl.lodz.p.it.opinioncollector.userModule.token.TokenRepository;
import pl.lodz.p.it.opinioncollector.userModule.token.TokenType;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class UserManager implements UserDetailsService {

    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder encoder;
    private final MailManager mailManager;

    @Override
    public User loadUserByUsername(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }


    private Token generateAndSaveDeletionToken(User user) {
        Token deletionToken = new Token();
        deletionToken.setUser(user);
        deletionToken.setToken(UUID.randomUUID().toString());
        deletionToken.setType(TokenType.DELETION_TOKEN);
        deletionToken.setCreatedAt(Instant.now());
        return tokenRepository.save(deletionToken);
    }

    public void changePassword(String oldPassword, String newPassword) throws PasswordNotMatchesException {
        //TODO check if password is strong enough
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = loadUserByUsername(email);
        if (encoder.matches(oldPassword, user.getPassword())) {
            user.setPassword(encoder.encode(newPassword));
            userRepository.save(user);
        } else {
            throw new PasswordNotMatchesException();
        }
    }

    public void lockUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST));
        try {
            user.setLocked(true);
            userRepository.save(user);
            tokenRepository.deleteTokenByUser(user);
            mailManager.adminActionEmail(user.getEmail(), user.getUsername(), "blocked");
        } catch (Exception e) {
            throw new EmailAlreadyRegisteredException();
        }
    }

    public void unlockUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST));
        try {
            user.setLocked(false);
            userRepository.save(user);
            mailManager.adminActionEmail(user.getEmail(), user.getVisibleName(), "unlocked");
        } catch (Exception e) {
            throw new EmailAlreadyRegisteredException();
        }
    }

    public void removeUserByUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = loadUserByUsername(email);
        try {
            String deletionToken = generateAndSaveDeletionToken(user).getToken();
            String link = "http://localhost:8080/api/confirm/remove?token=" + deletionToken;
            mailManager.deletionEmail(user.getEmail(), user.getVisibleName(), link);
        } catch (Exception e) {
            throw new EmailAlreadyRegisteredException();
        }
    }

    public void confirmDeletion(String token) {
        Token deletionToken = tokenRepository.findByToken(token)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST));
        tokenRepository.deleteTokenByUser(deletionToken.getUser());
        tokenRepository.deleteTokenByToken(token);
        userRepository.deleteUserByEmail(deletionToken.getUser().getEmail());
    }

    public void removeUserByAdmin(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST));
        try {
            userRepository.deleteUserByEmail(user.getEmail());
            tokenRepository.deleteTokenByUser(user);
            mailManager.adminActionEmail(user.getEmail(), user.getVisibleName(), "deleted");
        } catch (Exception e) {
            throw new EmailAlreadyRegisteredException();
        }
    }

    public void changeUsername(String newUsername) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = loadUserByUsername(email);
        user.setVisibleName(newUsername);
        userRepository.save(user);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}
