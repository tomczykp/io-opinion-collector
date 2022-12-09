package pl.lodz.p.it.opinioncollector.userModule.user;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChangePasswordDTO {
    @NotNull
    String oldPassword;

    @NotNull
    String newPassword;
}
