package pl.lodz.p.it.opinioncollector.userModule.auth;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import pl.lodz.p.it.opinioncollector.exceptions.user.EmailAlreadyRegisteredException;
import pl.lodz.p.it.opinioncollector.exceptions.user.TokenExpiredException;
import pl.lodz.p.it.opinioncollector.userModule.dto.LoginDTO;
import pl.lodz.p.it.opinioncollector.userModule.dto.RegisterUserDTO;
import pl.lodz.p.it.opinioncollector.userModule.dto.SuccessfulLoginDTO;
import pl.lodz.p.it.opinioncollector.userModule.user.User;

import java.io.IOException;
import java.net.URI;
import java.security.GeneralSecurityException;


@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthManager authManager;

    @Value("${frontend.url}")
    private String frontendUrl;


    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/register")
    public User register(@Valid @RequestBody RegisterUserDTO dto) throws Exception {
        return authManager.register(dto);
    }

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public SuccessfulLoginDTO login(@Valid @RequestBody LoginDTO dto) {
        return authManager.login(dto);
    }

    @GetMapping("/login/oauth2/code/google")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Void> authenticateByGoogle(@Param("code") String code) {
        return ResponseEntity.status(HttpStatus.FOUND).location(URI.create(frontendUrl + "/login?google_code=" + code)).build();
    }

    @GetMapping("/login/oauth2/code/facebook")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Void> authenticateByFacebook(@Param("code") String code) {
        return ResponseEntity.status(HttpStatus.FOUND).location(URI.create(frontendUrl + "/login?facebook_code=" + code)).build();
    }

    @GetMapping("/token/google")
    @ResponseStatus(HttpStatus.OK)
    public SuccessfulLoginDTO getTokenByGoogleCode(@Param("code") String code) throws GeneralSecurityException, IOException, IllegalAccessException {
        return authManager.authenticateWithGoogle(code);
    }

    @GetMapping("/token/facebook")
    @ResponseStatus(HttpStatus.OK)
    public SuccessfulLoginDTO getTokenByFacebookCode(@Param("code") String code) {
        return authManager.authenticateWithFacebook(code);
    }

    @GetMapping("/confirm/register")
    public ResponseEntity<Void> confirmRegistration(@Param("token") String token) {
        try {
            authManager.confirmRegistration(token);
        } catch (TokenExpiredException e) {
            return ResponseEntity.status(HttpStatus.FOUND).location(URI.create(frontendUrl + "/login?token-expired=true")).build();
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(HttpStatus.FOUND).location(URI.create(frontendUrl + "/login?token-deleted=true")).build();
        }
        return ResponseEntity.status(HttpStatus.FOUND).location(URI.create(frontendUrl + "/login?confirmed=true")).build();
    }

    @GetMapping("/confirm/remove")
    public ResponseEntity<Void> confirmDeletion(@Param("token") String token) {
        try {
            authManager.confirmDeletion(token);
        } catch (TokenExpiredException e) {
            return ResponseEntity.status(HttpStatus.FOUND).location(URI.create(frontendUrl + "/login?deletion-expired=true")).build();
        }  catch (ResponseStatusException e) {
            return ResponseEntity.status(HttpStatus.FOUND).location(URI.create(frontendUrl + "/login?token-deleted=true")).build();
        }
        return ResponseEntity.status(HttpStatus.FOUND).location(URI.create(frontendUrl + "/login?deleted=true")).build();
    }

    @GetMapping("/refresh")
    public SuccessfulLoginDTO refreshToken(@Param("token") String token) {
        return authManager.refresh(token);
    }

    @DeleteMapping("/signout")
    public void signout(@Param("token") String token) {
        authManager.dropToken(token);
    }

    @DeleteMapping("/signout/force")
    @ResponseStatus(HttpStatus.OK)
    public void forceSignout() {
        authManager.dropAllRefreshTokens();
    }
}
