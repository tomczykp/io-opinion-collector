package pl.lodz.p.it.opinioncollector.userModule.auth;


import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import pl.lodz.p.it.opinioncollector.userModule.user.User;

import jakarta.validation.Valid;


@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthManager authManager;


    @PostMapping("/register")
    public User register(@Valid @RequestBody RegisterUserDTO dto) {
        return authManager.register(dto);
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

}
