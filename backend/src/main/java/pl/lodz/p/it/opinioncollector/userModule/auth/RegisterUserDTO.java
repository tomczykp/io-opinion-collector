package pl.lodz.p.it.opinioncollector.userModule.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterUserDTO {
    @NotNull
    String email;

    @NotNull
    String username;

    @NotNull
    String password;
}
