package pl.lodz.p.it.opinioncollector.userModule.user;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
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

    @PostMapping("/changePassword")
    public ResponseEntity<String> changePassword(@Param("oldPassword") String oldPassword, @Param("newPassword") String newPassword) {
        try {
            userManager.changePassword(oldPassword, newPassword);
            return ResponseEntity.ok("Password has been changed");
        } catch (PasswordNotMatchesException e) {
            return ResponseEntity.status(401).body("Wrong password");
        }
    }

    @PostMapping("/lock")
    public String lockUser(@NotNull @Param("id") Long id) {
        userManager.lockUser(id);
        return "user locked";
    }

    @PostMapping("/unlock")
    public String unlockUser(@NotNull @Param("id") Long id) {
        userManager.unlockUser(id);
        return "user unlocked";
    }

    @DeleteMapping("/remove/admin")
    public String removeUserByAdmin(@NotNull @Param("id") Long id) {
        userManager.removeUserByAdmin(id);
        return "user deleted by admin";
    }

    @DeleteMapping("/remove/user")
    public String removeUserByUser() {
        userManager.removeUserByUser();
        return "deletion confirmation email send";
    }

    @GetMapping("/confirm/remove")
    public String confirmDeletion(@Param("token") String token) {
        userManager.confirmDeletion(token);
        return "Confirmed Deletion!";
    }

    @PostMapping("/changeUsername")
    public String changeUsername(@Param("newUsername") String newUsername) {
        userManager.changeUsername(newUsername);
        return "username has been changed";
    }

    @GetMapping("/getAll")
    public ResponseEntity getAll() {
        List<User> userList = userManager.getAllUsers();
        return ResponseEntity.ok(userList);
    }
}
