package pl.lodz.p.it.opinioncollector.userModule.auth;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import pl.lodz.p.it.opinioncollector.exceptions.EmailAlreadyRegisteredException;
import pl.lodz.p.it.opinioncollector.userModule.dto.LoginDTO;
import pl.lodz.p.it.opinioncollector.userModule.dto.RegisterUserDTO;
import pl.lodz.p.it.opinioncollector.userModule.dto.SuccessfulLoginDTO;
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

    @GetMapping("/confirm/register")
    public String confirmRegistration(@Param("token") String token) {
        authManager.confirmRegistration(token);
        return "Confirmed!";
    }

    @GetMapping("/confirm/remove")
    public String confirmDeletion(@Param("token") String token) {
        authManager.confirmDeletion(token);
        return "Confirmed Deletion!";
    }

    @GetMapping("/refresh")
    public String refreshToken(@Param("token") String token) {
        return authManager.validateAndRenewRefreshToken(token);
    }

    @DeleteMapping("/signout")
    public void signout(@Param("token") String token) {
        authManager.dropRefreshToken(token);
    }

    @DeleteMapping("/signout/force")
    @ResponseStatus(HttpStatus.OK)
    public void forceSignout() {
        authManager.dropAllRefreshTokens();
    }
}
