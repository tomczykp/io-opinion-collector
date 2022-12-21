package pl.lodz.p.it.opinioncollector.userModule.user;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import pl.lodz.p.it.opinioncollector.exceptions.user.PasswordNotMatchesException;
import pl.lodz.p.it.opinioncollector.userModule.auth.MailManager;
import pl.lodz.p.it.opinioncollector.userModule.token.Token;
import pl.lodz.p.it.opinioncollector.userModule.token.TokenRepository;
import pl.lodz.p.it.opinioncollector.userModule.token.TokenType;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class UserManager implements UserDetailsService {
    //TODO validate tokens expiration date and create cron to clear expired tokens + delete account if verification token expired and user is not active

    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder encoder;
    private final MailManager mailManager;

    @Value("${backend.url}")
    private String apiUrl;

    @Value("${frontend.url}")
    private String frontendUrl;

    @Override
    public User loadUserByUsername(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    private Token generateAndSaveToken(User user, TokenType tokenType) {
        Token token = new Token(UUID.randomUUID().toString(), tokenType, user);
        return tokenRepository.save(token);
    }

    public void changeUsername(String newUsername) {
        try {
            User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            user.setVisibleName(newUsername);
            userRepository.save(user);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }
    }

    public void sendResetPassword(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST));

        if (tokenRepository.findTokenByUserAndType(user, TokenType.PASSWORD_RESET_TOKEN).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }

        String resetPasswordToken = generateAndSaveToken(user, TokenType.PASSWORD_RESET_TOKEN).getToken();
        String link = frontendUrl + "/resetConfirm/" + resetPasswordToken;
        mailManager.resetPasswordEmail(email, "your email " + email, link);

    }

    public void resetPassword(String newPassword, String resetToken) {
        Token token = tokenRepository.findByToken(resetToken)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST));

        User user = token.getUser();
        user.setPassword(encoder.encode(newPassword));

        userRepository.save(user);
        tokenRepository.deleteByToken(resetToken);
    }

    public void changePassword(String oldPassword, String newPassword) throws PasswordNotMatchesException {
        //TODO check if password is strong enough
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (encoder.matches(oldPassword, user.getPassword())) {
            user.setPassword(encoder.encode(newPassword));
            userRepository.save(user);
        } else {
            throw new PasswordNotMatchesException();
        }
    }

    public void deleteOwnAccount() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (tokenRepository.findTokenByUserAndType(user, TokenType.DELETION_TOKEN).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        String deletionToken = generateAndSaveToken(user, TokenType.DELETION_TOKEN).getToken();
        String link = apiUrl + "/confirm/remove?token=" + deletionToken;
        mailManager.deletionEmail(user.getEmail(), user.getVisibleName(), link);
    }

    public void removeUserByAdmin(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST));

        userRepository.deleteUserByEmail(user.getEmail());
        tokenRepository.deleteAllByUser(user);
        mailManager.adminActionEmail(user.getEmail(), user.getVisibleName(), "deleted");
    }

    public void lockUser(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST));
        user.setLocked(true);

        userRepository.save(user);
        tokenRepository.deleteAllByUserAndType(user, TokenType.REFRESH_TOKEN);

        mailManager.adminActionEmail(user.getEmail(), user.getUsername(), "blocked");
    }

    public void unlockUser(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST));
        user.setLocked(false);

        userRepository.save(user);
        mailManager.adminActionEmail(user.getEmail(), user.getVisibleName(), "unlocked");
    }

    public List<User> getAllUsers(String email) {
        if (email == null || email.equals("")) {
            return userRepository.findAll();
        }
        return userRepository.findByEmailContainingIgnoreCase(email);
    }
}
