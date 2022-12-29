package pl.lodz.p.it.opinioncollector.exceptions.category;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class FieldNotFoundException extends Exception {
    public FieldNotFoundException(){

    }

    public FieldNotFoundException(Exception cause){super(cause);}

    public FieldNotFoundException(String message){super(message);}

    public FieldNotFoundException(String message, Exception cause){super(message,cause);}

}
