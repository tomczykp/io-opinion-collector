package pl.lodz.p.it.opinioncollector.userModule.user;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import net.minidev.json.JSONObject;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;
import pl.lodz.p.it.opinioncollector.userModule.auth.LoginDTO;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserManager userManager;

    @GetMapping("/")
    public String greet() {
        return "Welcome to OpinionCollector!";
    }

    @PostMapping("/changePassword")
    public String changePassword(@Param("oldPassword") String oldPassword, @Param("newPassword") String newPassword, HttpServletRequest request) {
        return userManager.changePassword(oldPassword, newPassword, request);
    }

    @PostMapping("/lockUser")
    public String lockUser(@NotNull @RequestBody User user) {
        userManager.lockUser(user);
        return "user locked";
    }

    @PostMapping("/unlockUser")
    public String unlockUser(@NotNull @RequestBody User user) {
        userManager.unlockUser(user);
        return "user unlocked";
    }

    @DeleteMapping("/remove/admin")
    public String removeUserByAdmin(@NotNull @RequestBody User user) {
        userManager.removeUserByAdmin(user);
        return "user deleted by admin";
    }

    @DeleteMapping("/remove/user")
    public String removeUserByUser(HttpServletRequest request) {
        userManager.removeUserByUser(request);
        return "deletion confirmation email send";
    }

    @GetMapping("/remove/confirm")
    public String confirmDeletion(@Param("token") String token) {
        userManager.confirmDeletion(token);
        return "Confirmed Deletion!";
    }

    @PostMapping("/changeUsername")
    public String changeUsername(@Param("newUsername") String newUsername, HttpServletRequest request) {
        userManager.changeUsername(newUsername, request);
        return "username has been changed";
    }

    @GetMapping("/getAll")
    public ResponseEntity getAll() {
        List<User> userList = userManager.getAllUsers();
        return ResponseEntity.ok(userList);
    }
}
