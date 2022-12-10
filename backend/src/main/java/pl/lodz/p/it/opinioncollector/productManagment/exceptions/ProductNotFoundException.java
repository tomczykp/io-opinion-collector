package pl.lodz.p.it.opinioncollector.productManagment.exceptions;



public class ProductNotFoundException extends Exception {
    public ProductNotFoundException() {
    }

    public ProductNotFoundException(Exception cause) {
        super(cause);
    }

    public ProductNotFoundException(String message) {
        super(message);
    }

    public ProductNotFoundException(String message, Exception cause) {
        super(message, cause);
    }
}
