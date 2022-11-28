package pl.lodz.p.it.opinioncollector.user;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @GetMapping("/")
    public String greet() {
        return "Welcome to OpinionCollector!";
    }
}
