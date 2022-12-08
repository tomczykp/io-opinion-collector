package pl.lodz.p.it.opinioncollector.userModule.user;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;


@RestController
@RequiredArgsConstructor
@CrossOrigin
@RequestMapping("/users")
public class UserController {

    private final UserManager userManager;

    @PutMapping("/password")
    @ResponseStatus(HttpStatus.OK)
    public void changePassword(@Valid @RequestBody ChangePasswordDTO dto) throws PasswordNotMatchesException {
        try {
            userManager.changePassword(dto.oldPassword, dto.newPassword);
        } catch (PasswordNotMatchesException e) {
            throw new PasswordNotMatchesException();
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

    @GetMapping("/remove/confirm")
    public String confirmDeletion(@Param("token") String token) {
        userManager.confirmDeletion(token);
        return "Confirmed Deletion!";
    }

    @PostMapping("/username")
    @ResponseStatus(HttpStatus.OK)
    public void changeUsername(@Param("newUsername") String newUsername) {
        try {
            userManager.changeUsername(newUsername);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }

    }

    @GetMapping
    public List<User> getAll() {
        return userManager.getAllUsers();
    }

    @GetMapping("/{email}")
    public User getByEmail(@PathVariable("email") String email) {
        return userManager.loadUserByUsername(email);
    }
}
