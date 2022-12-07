package pl.lodz.p.it.opinioncollector.userModule.user;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@CrossOrigin
public class UserController {

    private final UserManager userManager;

    @GetMapping("/")
    public String greet() {
        return "Welcome to OpinionCollector!";
    }
}
