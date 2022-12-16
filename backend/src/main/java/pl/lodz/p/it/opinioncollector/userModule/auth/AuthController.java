package pl.lodz.p.it.opinioncollector.userModule.auth;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.lodz.p.it.opinioncollector.exceptions.user.EmailAlreadyRegisteredException;
import pl.lodz.p.it.opinioncollector.userModule.dto.LoginDTO;
import pl.lodz.p.it.opinioncollector.userModule.dto.RegisterUserDTO;
import pl.lodz.p.it.opinioncollector.userModule.dto.SuccessfulLoginDTO;
import pl.lodz.p.it.opinioncollector.userModule.user.User;

import java.net.URI;


@RestController
@RequiredArgsConstructor
@CrossOrigin
public class AuthController {

    private final AuthManager authManager;

    @Value("${frontend.url}")
    private String frontendUrl;


    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/register")
    public User register(@Valid @RequestBody RegisterUserDTO dto) throws EmailAlreadyRegisteredException {
        return authManager.register(dto);
    }

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public SuccessfulLoginDTO login(@Valid @RequestBody LoginDTO dto) {
        return authManager.login(dto);
    }

    @GetMapping("/confirm/register")
    public ResponseEntity<Void> confirmRegistration(@Param("token") String token) {
        authManager.confirmRegistration(token);
        return ResponseEntity.status(HttpStatus.FOUND).location(URI.create(frontendUrl + "/login")).build();
    }

    @GetMapping("/confirm/remove")
    public ResponseEntity<Void> confirmDeletion(@Param("token") String token) {
        authManager.confirmDeletion(token);
        return ResponseEntity.status(HttpStatus.FOUND).location(URI.create(frontendUrl + "/login")).build();
        //TODO somehow log out user on frontend
    }

    @GetMapping("/refresh")
    public String refreshToken(@Param("token") String token) {
        return authManager.validateAndRenewRefreshToken(token);
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
