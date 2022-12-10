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
    @ResponseStatus(HttpStatus.OK)
    public void lockUser(@NotNull @Param("id") Long id) {
        userManager.lockUser(id);
    }

    @PostMapping("/unlock")
    @ResponseStatus(HttpStatus.OK)
    public void unlockUser(@NotNull @Param("id") Long id) {
        userManager.unlockUser(id);
    }

    @DeleteMapping("/remove/admin")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeUserByAdmin(@NotNull @Param("id") Long id) {
        userManager.removeUserByAdmin(id);
    }

    @DeleteMapping("/remove/user")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeUserByUser() {
        userManager.removeUserByUser();
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
    @ResponseStatus(HttpStatus.OK)
    public List<User> getAll(@Param("email") String email) {
        return userManager.getAllUsers(email);
    }

    @GetMapping("/{email}")
    public User getByEmail(@PathVariable("email") String email) {
        return userManager.loadUserByUsername(email);
    }

    @PutMapping("/reset")
    @ResponseStatus(HttpStatus.OK)
    public void resetPassword(@Param("email") String email) {
        userManager.sendResetPassword(email);
    }

    @PutMapping("/confirm/reset")
    @ResponseStatus(HttpStatus.OK)
    public void confirmReset(@Param("password") String password, @Param("token") String token) {
        userManager.resetPassword(password, token);
    }
}
