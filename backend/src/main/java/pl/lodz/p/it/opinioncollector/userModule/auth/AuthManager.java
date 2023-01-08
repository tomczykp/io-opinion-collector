package pl.lodz.p.it.opinioncollector.userModule.auth;


import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;
import pl.lodz.p.it.opinioncollector.exceptions.user.EmailAlreadyRegisteredException;
import pl.lodz.p.it.opinioncollector.exceptions.user.TokenExpiredException;
import pl.lodz.p.it.opinioncollector.userModule.dto.LoginDTO;
import pl.lodz.p.it.opinioncollector.userModule.dto.RegisterUserDTO;
import pl.lodz.p.it.opinioncollector.userModule.dto.SuccessfulLoginDTO;
import pl.lodz.p.it.opinioncollector.userModule.token.Token;
import pl.lodz.p.it.opinioncollector.userModule.token.TokenRepository;
import pl.lodz.p.it.opinioncollector.userModule.token.TokenType;
import pl.lodz.p.it.opinioncollector.userModule.user.User;
import pl.lodz.p.it.opinioncollector.userModule.user.UserProvider;
import pl.lodz.p.it.opinioncollector.userModule.user.UserRepository;
import pl.lodz.p.it.opinioncollector.userModule.user.UserType;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.time.Instant;
import java.util.Collections;
import java.util.Optional;
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
    private final HttpTransport transport = new NetHttpTransport();
    private final JsonFactory factory = new GsonFactory();
    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${backend.url}")
    private String apiUrl;
    @Value("${spring.security.oauth2.client.registration.google.clientId}")
    private String googleClientId;
    @Value("${spring.security.oauth2.client.registration.google.clientSecret}")
    private String googleClientSecret;
    @Value("${spring.security.oauth2.client.registration.facebook.clientId}")
    private String facebookClientId;
    @Value("${spring.security.oauth2.client.registration.facebook.clientSecret}")
    private String facebookClientSecret;

    public SuccessfulLoginDTO login(LoginDTO dto) {
        Authentication authentication;
        try {
            authentication = authenticationManager.
                    authenticate(new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPassword()));
        } catch (LockedException le) {
            throw new ResponseStatusException(HttpStatus.LOCKED);
        } catch (DisabledException de) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE);
        } catch (AuthenticationException ae) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

        User user = (User) authentication.getPrincipal();

        if (user.isDeleted()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

        String jwt = jwtProvider.generateJWT(user.getEmail());
        Token refreshToken = generateAndSaveToken(user, TokenType.REFRESH_TOKEN);
        return new SuccessfulLoginDTO(user.getRole(), jwt, refreshToken.getToken(), user.getEmail(), user.getVisibleName(), user.getProvider());
    }

    public User register(RegisterUserDTO dto) throws Exception {
        User user;
        Optional<User> u = userRepository.findByEmail(dto.getEmail());
        if (u.isPresent()) {
            user = u.get();
            Optional<Token> t = tokenRepository.findTokenByUserAndType(u.get(), TokenType.VERIFICATION_TOKEN);
            if (t.isPresent()) {
                if (t.get().getExpiresAt().isAfter(Instant.now())) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
                }
                tokenRepository.delete(t.get());

                String hashedPassword = encoder.encode(dto.getPassword());
                user.setVisibleName(dto.getUsername());
                user.setPassword(hashedPassword);
            } else if (u.get().isActive()) {
                throw new EmailAlreadyRegisteredException();
            }
        } else {
            String hashedPassword = encoder.encode(dto.getPassword());
            user = new User(dto.getEmail(), dto.getUsername(), hashedPassword);
        }

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
        Token token = new Token(UUID.randomUUID().toString(), tokenType, user);
        return tokenRepository.save(token);
    }

    public void confirmRegistration(String token) throws TokenExpiredException {
        Token verificationToken = tokenRepository.findByToken(token)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST));

        if (verificationToken.getExpiresAt().isBefore(Instant.now())) {
            throw new TokenExpiredException();
        }
        User user = verificationToken.getUser();
        user.setActive(true);

        userRepository.save(user);
        tokenRepository.delete(verificationToken);
    }

    public SuccessfulLoginDTO refresh(String token) {
        Token t = tokenRepository.findByToken(token)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST));

        if (t.getExpiresAt().isBefore(Instant.now())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Refresh token has expired!");
        }

        User user = t.getUser();

        String newJWT = jwtProvider.generateJWT(user.getEmail());

        return new SuccessfulLoginDTO(user.getRole(), newJWT, token, user.getEmail(), user.getVisibleName(), user.getProvider());
    }

    public void confirmDeletion(String token) throws TokenExpiredException {
        Token deletionToken = tokenRepository.findByToken(token)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST));

        if (deletionToken.getExpiresAt().isBefore(Instant.now())) {
            throw new TokenExpiredException();
        }

        tokenRepository.deleteAllByUser(deletionToken.getUser());
        User user = userRepository.findByEmail(deletionToken.getUser().getEmail())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST));
        user.setDeleted(true);
    }

    public void dropToken(String token) {
        try {
            tokenRepository.deleteByToken(token);
        } catch (Exception ignored) {

        }
    }

    public void dropAllRefreshTokens() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        tokenRepository.deleteAllByUserAndType(user, TokenType.REFRESH_TOKEN);
    }

    public SuccessfulLoginDTO authenticateWithGoogle(String code) {

        String id_token = exchangeGoogleCodeForIdToken(code);
        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(transport, factory).setAudience(Collections.singleton(googleClientId)).build();

        GoogleIdToken idToken;
        try {
            idToken = verifier.verify(id_token);
        } catch (GeneralSecurityException | IOException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        if (idToken == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        User user = getExistingOrRegisterUser(idToken.getPayload().getEmail(), UserProvider.GOOGLE);

        String jwt = jwtProvider.generateJWT(user.getEmail());
        Token refreshToken = generateAndSaveToken(user, TokenType.REFRESH_TOKEN);
        return new SuccessfulLoginDTO(user.getRole(), jwt, refreshToken.getToken(), user.getEmail(), user.getVisibleName(), user.getProvider());
    }

    private String exchangeGoogleCodeForIdToken(String code) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("grant_type", "authorization_code");
        jsonObject.addProperty("code", code);
        jsonObject.addProperty("client_id", googleClientId);
        jsonObject.addProperty("client_secret", googleClientSecret);
        jsonObject.addProperty("redirect_uri", apiUrl + "/login/oauth2/code/google");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> request = new HttpEntity<>(jsonObject.toString(), headers);

        String result = restTemplate.postForObject("https://accounts.google.com/o/oauth2/token", request, String.class);
        JsonObject resultJson = new Gson().fromJson(result, JsonObject.class);

        return resultJson.get("id_token").getAsString();
    }

    public SuccessfulLoginDTO authenticateWithFacebook(String code) {
        String email;
        try {
            String accessToken = exchangeFacebookCodeForAccessToken(code);
            String id = getFacebookUserId(accessToken);
            email = getFacebookUserEmail(id, accessToken);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        User user = getExistingOrRegisterUser(email, UserProvider.FACEBOOK);

        String jwt = jwtProvider.generateJWT(user.getEmail());
        Token refreshToken = generateAndSaveToken(user, TokenType.REFRESH_TOKEN);
        return new SuccessfulLoginDTO(user.getRole(), jwt, refreshToken.getToken(), user.getEmail(), user.getVisibleName(), user.getProvider());
    }

    private String exchangeFacebookCodeForAccessToken(String code) {
        String url = new StringBuilder()
                .append("https://graph.facebook.com/oauth/access_token?grant_type=authorization_code&client_id=")
                .append(facebookClientId)
                .append("&client_secret=")
                .append(facebookClientSecret)
                .append("&code=")
                .append(code)
                .append("&redirect_uri=")
                .append(apiUrl)
                .append("/login/oauth2/code/facebook")
                .toString();

        String result = restTemplate.getForObject(url, String.class);
        JsonObject resultJson = new Gson().fromJson(result, JsonObject.class);

        return resultJson.get("access_token").getAsString();
    }

    private String getFacebookUserId(String accessToken) {

        String url = "https://graph.facebook.com/me?access_token=" +
                accessToken;

        String result = restTemplate.getForObject(url, String.class);
        JsonObject resultJson = new Gson().fromJson(result, JsonObject.class);

        return resultJson.get("id").getAsString();
    }

    private String getFacebookUserEmail(String id, String accessToken) {

        String url = new StringBuilder()
                .append("https://graph.facebook.com/")
                .append(id)
                .append("?fields=email&access_token=")
                .append(accessToken)
                .toString();

        String result = restTemplate.getForObject(url, String.class);
        JsonObject resultJson = new Gson().fromJson(result, JsonObject.class);

        return resultJson.get("email").getAsString();
    }

    private User getExistingOrRegisterUser(String email, UserProvider provider) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        User user;
        if (optionalUser.isEmpty()) {
            user = new User();
            user.setEmail(email);
            user.setVisibleName(email);
            user.setRole(UserType.USER);
            user.setProvider(provider);
            user.setActive(true);
            return userRepository.save(user);
        } else {
            user = optionalUser.get();
            if (user.getProvider() != provider) {
                throw new ResponseStatusException(HttpStatus.CONFLICT);
            }

            if (user.isDeleted()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND);
            }

            if (user.isLocked()) {
                throw new ResponseStatusException(HttpStatus.LOCKED);
            }
            return user;
        }
    }
}
