package pl.lodz.p.it.opinioncollector.productManagment.exceptions;


import jakarta.persistence.EntityNotFoundException;

public class ProductNotFoundException extends EntityNotFoundException {
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
