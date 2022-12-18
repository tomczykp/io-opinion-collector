package pl.lodz.p.it.opinioncollector.exceptions.category;

import pl.lodz.p.it.opinioncollector.productManagment.exceptions.ProductNotFoundException;

public class CategoryNotFoundException extends Exception {
    public CategoryNotFoundException(){

    }

    public CategoryNotFoundException(Exception cause){super(cause);}

    public CategoryNotFoundException(String message){super(message);}

    public CategoryNotFoundException(String message, Exception cause){super(message,cause);}

}
