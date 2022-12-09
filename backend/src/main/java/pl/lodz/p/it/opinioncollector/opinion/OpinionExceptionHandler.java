package pl.lodz.p.it.opinioncollector.opinion;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import pl.lodz.p.it.opinioncollector.opinion.exceptions.OpinionNotFoundException;
import pl.lodz.p.it.opinioncollector.opinion.exceptions.OpinionOperationAccessForbiddenException;

@ControllerAdvice
@ResponseBody
public class OpinionExceptionHandler {

    @ExceptionHandler({
        OpinionNotFoundException.class
    })
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleNotFoundException(Exception e) {
        return e.getMessage();
    }

    @ExceptionHandler({
        OpinionOperationAccessForbiddenException.class
    })
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public String handleAccessForbidenException(Exception e) {
        return e.getMessage();
    }
}
