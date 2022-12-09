package pl.lodz.p.it.opinioncollector.userModule.user;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class PasswordNotMatchesException extends Exception {
}
