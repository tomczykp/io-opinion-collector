package pl.lodz.p.it.opinioncollector.userModule.auth;


import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthManager authManager;

}
