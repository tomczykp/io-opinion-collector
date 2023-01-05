package pl.lodz.p.it.opinioncollector.userModule.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResetPasswordDTO {
    @NotNull
    private String password;

    @NotNull
    private String token;
}