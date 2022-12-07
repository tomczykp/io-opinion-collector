package pl.lodz.p.it.opinioncollector.userModule.user;


import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
    private final JwtProvider jwtProvider;
    private final MailManager mailManager;

    @Override
    public UserDetails loadUserByUsername(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    private User getUserFromJwt(String jwt) {
        Claims claims = jwtProvider.parseJWT(jwt).getBody();
        UserDetails userDetails = loadUserByUsername(claims.getSubject());
        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST));
        return user;
    }

    private Token generateAndSaveRefreshToken(User user) {
        Token verificationToken = new Token();
        verificationToken.setUser(user);
        verificationToken.setToken(UUID.randomUUID().toString());
        verificationToken.setType(TokenType.REFRESH_TOKEN);
        verificationToken.setCreatedAt(Instant.now());
        return tokenRepository.save(verificationToken);
    }

    private Token generateAndSaveDeletionToken(User user) {
        Token deletionToken = new Token();
        deletionToken.setUser(user);
        deletionToken.setToken(UUID.randomUUID().toString());
        deletionToken.setType(TokenType.DELETION_TOKEN);
        deletionToken.setCreatedAt(Instant.now());
        return tokenRepository.save(deletionToken);
    }

    public String changePassword(String oldPassword, String newPassword, HttpServletRequest httpServletRequest) {
        String jwt = jwtProvider.getToken(httpServletRequest);
        User user = getUserFromJwt(jwt);
        if (encoder.matches(oldPassword, user.getPassword())) {
            tokenRepository.deleteTokenByToken(tokenRepository.findTokenByUser(userRepository.findByEmail(user.getUsername()).orElseThrow()).orElseThrow().getToken());
            user.setPassword(encoder.encode(newPassword));
            userRepository.save(user);
            this.generateAndSaveRefreshToken(user);
            return jwtProvider.generateJWT(user.getEmail());
        }
        return "Nie poprawne haslo!";
    }

    public void lockUser(User user) {
        user.setLocked(true);
        userRepository.save(user);
        tokenRepository.deleteTokenByUser(user);
        mailManager.adminActionEmail(user.getEmail(), user.getUsername(), "blocked");
    }

    public void unlockUser(User user) {
        user.setLocked(false);
        userRepository.save(user);
        mailManager.adminActionEmail(user.getEmail(), user.getVisibleName(), "unlocked");
    }

    public void removeUserByUser(HttpServletRequest httpServletRequest) {
        String jwt = jwtProvider.getToken(httpServletRequest);
        User user = getUserFromJwt(jwt);
        String deletionToken = generateAndSaveDeletionToken(user).getToken();
        String link = "http://localhost:8080/api/remove/confirm?token=" + deletionToken;
        mailManager.deletionEmail(user.getEmail(), user.getVisibleName(), link);
    }

    public void confirmDeletion(String token) {
        Token deletionToken = tokenRepository.findByToken(token)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST));
        tokenRepository.deleteTokenByUser(deletionToken.getUser());
        tokenRepository.deleteTokenByToken(token);
        userRepository.deleteUserByEmail(deletionToken.getUser().getEmail());
    }

    public void removeUserByAdmin(User user) {
        userRepository.deleteUserByEmail(user.getEmail());
        tokenRepository.deleteTokenByUser(user);
        mailManager.adminActionEmail(user.getEmail(), user.getVisibleName(), "deleted");
    }

    public void changeUsername(String newUsername, HttpServletRequest request) {
        String jwt = jwtProvider.getToken(request);
        User user = getUserFromJwt(jwt);
        user.setVisibleName(newUsername);
        userRepository.save(user);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}
