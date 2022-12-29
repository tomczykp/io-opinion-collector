package pl.lodz.p.it.opinioncollector.exceptions.user;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import pl.lodz.p.it.opinioncollector.exceptions.BaseApplicationException;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class TokenDeletedException extends BaseApplicationException {
}
