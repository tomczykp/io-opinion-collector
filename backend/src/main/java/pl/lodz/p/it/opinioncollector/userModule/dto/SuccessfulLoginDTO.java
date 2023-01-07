package pl.lodz.p.it.opinioncollector.userModule.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import pl.lodz.p.it.opinioncollector.userModule.user.UserProvider;
import pl.lodz.p.it.opinioncollector.userModule.user.UserType;

@AllArgsConstructor
@Data
public class SuccessfulLoginDTO {
    private UserType role;
    private String jwt;
    private String refreshToken;
    private String email;
    private String visibleName;
    private UserProvider provider;
}
