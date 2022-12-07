package pl.lodz.p.it.opinioncollector.userModule.auth;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import pl.lodz.p.it.opinioncollector.userModule.user.User;


@RestController
@RequiredArgsConstructor
@CrossOrigin
public class AuthController {

    private final AuthManager authManager;


    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/register")
    public User register(@Valid @RequestBody RegisterUserDTO dto) {
        try {
            return authManager.register(dto);
        } catch (EmailAlreadyRegisteredException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }
    }

    @PostMapping("/login")
    public SuccessfulLoginDTO login(@Valid @RequestBody LoginDTO dto) {
        return authManager.login(dto);
    }

    @GetMapping("/register/confirm")
    public String confirmRegistration(@Param("token") String token) {
        authManager.confirmRegistration(token);
        return "Confirmed!";
    }

    @GetMapping("/refresh")
    public String refreshToken(@Param("token") String token) {
        return authManager.validateAndRenewRefreshToken(token);
    }

    @GetMapping("/signout")
    public String logout(@Param("token") String token) {
        authManager.deleteRefreshToken(token);
        return "LoggedOut!";
    }
}
