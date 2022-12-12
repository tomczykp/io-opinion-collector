package pl.lodz.p.it.opinioncollector.userModule.auth;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import pl.lodz.p.it.opinioncollector.exceptions.user.EmailAlreadyRegisteredException;
import pl.lodz.p.it.opinioncollector.userModule.dto.LoginDTO;
import pl.lodz.p.it.opinioncollector.userModule.dto.RegisterUserDTO;
import pl.lodz.p.it.opinioncollector.userModule.dto.SuccessfulLoginDTO;
import pl.lodz.p.it.opinioncollector.userModule.token.Token;
import pl.lodz.p.it.opinioncollector.userModule.token.TokenRepository;
import pl.lodz.p.it.opinioncollector.userModule.token.TokenType;
import pl.lodz.p.it.opinioncollector.userModule.user.User;
import pl.lodz.p.it.opinioncollector.userModule.user.UserRepository;

import java.time.Instant;
import java.util.UUID;


@Service
@RequiredArgsConstructor
@Transactional
public class AuthManager {

    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder encoder;
    private final JwtProvider jwtProvider;
    private final MailManager mailManager;

    @Value("${backend.url}")
    private String apiUrl;


    public SuccessfulLoginDTO login(LoginDTO dto) {
        Authentication authentication;
        try {
            authentication = authenticationManager.
                    authenticate(new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPassword()));
        } catch (LockedException | DisabledException e) {
            throw new ResponseStatusException(HttpStatus.LOCKED);
        }

        User user = (User) authentication.getPrincipal();
        String jwt = jwtProvider.generateJWT(user.getEmail(), user.getRole());
        Token refreshToken = generateAndSaveToken(user, TokenType.REFRESH_TOKEN);
        return new SuccessfulLoginDTO(user.getRole(), jwt, refreshToken.getToken(), user.getEmail());
    }

    public User register(RegisterUserDTO dto) throws EmailAlreadyRegisteredException {
        String hashedPassword = encoder.encode(dto.getPassword());
        User user = new User(dto.getEmail(), dto.getUsername(), hashedPassword);
        try {
            userRepository.save(user);
            String verificationToken = generateAndSaveToken(user, TokenType.VERIFICATION_TOKEN).getToken();
            String link = apiUrl + "/confirm/register?token=" + verificationToken;
            mailManager.registrationEmail(user.getEmail(), user.getVisibleName(), link);
            return user;
        } catch (Exception e) {
            throw new EmailAlreadyRegisteredException();
        }
    }

    private Token generateAndSaveToken(User user, TokenType tokenType) {
        Token verificationToken = new Token();
        verificationToken.setUser(user);
        verificationToken.setToken(UUID.randomUUID().toString());
        verificationToken.setType(tokenType);
        verificationToken.setCreatedAt(Instant.now());
        return tokenRepository.save(verificationToken);
    }

    public void confirmRegistration(String token) {
        Token verificationToken = tokenRepository.findByToken(token)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST));

        User user = verificationToken.getUser();
        user.setActive(true);

        userRepository.save(user);
        tokenRepository.delete(verificationToken);
    }

    public String validateAndRenewRefreshToken(String token) {
        Token t = tokenRepository.findByToken(token)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST));

        User user = t.getUser();
        return jwtProvider.generateJWT(user.getEmail(), user.getRole());
    }

    public void confirmDeletion(String token) {
        Token deletionToken = tokenRepository.findByToken(token)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST));

        tokenRepository.deleteAllByUser(deletionToken.getUser());
        userRepository.deleteUserByEmail(deletionToken.getUser().getEmail());
    }

    public void dropToken(String token) {
        tokenRepository.deleteByToken(token);
    }

    public void dropAllRefreshTokens() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        tokenRepository.deleteAllByUserAndType(user, TokenType.REFRESH_TOKEN);
    }
}
