package pl.lodz.p.it.opinioncollector.exceptions.category;

public class FieldNotFoundException extends Exception {
    public FieldNotFoundException(){

    }

    public FieldNotFoundException(Exception cause){super(cause);}

    public FieldNotFoundException(String message){super(message);}

    public FieldNotFoundException(String message, Exception cause){super(message,cause);}

}
