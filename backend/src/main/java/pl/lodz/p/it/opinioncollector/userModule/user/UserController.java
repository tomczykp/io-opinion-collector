package pl.lodz.p.it.opinioncollector.userModule.user;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import pl.lodz.p.it.opinioncollector.exceptions.user.PasswordNotMatchesException;
import pl.lodz.p.it.opinioncollector.exceptions.user.TokenExpiredException;
import pl.lodz.p.it.opinioncollector.userModule.dto.ChangePasswordDTO;

import java.util.List;


@RestController
@RequiredArgsConstructor
@CrossOrigin
@RequestMapping("/users")
public class UserController {

    private final UserManager userManager;

    @PutMapping("/password")
    @ResponseStatus(HttpStatus.OK)
    public void changePassword(@Valid @RequestBody ChangePasswordDTO dto) throws PasswordNotMatchesException {
        userManager.changePassword(dto.getOldPassword(), dto.getNewPassword());
    }

    @PutMapping("/lock")
    @ResponseStatus(HttpStatus.OK)
    public void lockUser(@NotNull @Param("email") String email) {
        userManager.lockUser(email);
    }

    @PutMapping("/unlock")
    @ResponseStatus(HttpStatus.OK)
    public void unlockUser(@NotNull @Param("email") String email) {
        userManager.unlockUser(email);
    }

    @DeleteMapping("/remove/admin")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeUserByAdmin(@NotNull @Param("email") String email) {
        userManager.removeUserByAdmin(email);
    }

    @DeleteMapping("/remove/user")
    @ResponseStatus(HttpStatus.OK)
    public void deleteOwnAccount() throws Exception {
        userManager.deleteOwnAccount();
    }

    @PutMapping("/username")
    @ResponseStatus(HttpStatus.OK)
    public void changeUsername(@NotNull @Param("newUsername") String newUsername) {
        userManager.changeUsername(newUsername);
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
    public void resetPassword(@Param("email") String email) throws Exception{
        userManager.sendResetPassword(email);
    }

    @PutMapping("/confirm/reset")
    @ResponseStatus(HttpStatus.OK)
    public void confirmReset(@Param("password") String password, @Param("token") String token) throws TokenExpiredException {
        userManager.resetPassword(password, token);
    }
}
