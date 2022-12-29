package pl.lodz.p.it.opinioncollector.exceptions.category;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class CategoryNotFoundException extends Exception {
    public CategoryNotFoundException(){

    }

    public CategoryNotFoundException(Exception cause){super(cause);}

    public CategoryNotFoundException(String message){super(message);}

    public CategoryNotFoundException(String message, Exception cause){super(message,cause);}

}
