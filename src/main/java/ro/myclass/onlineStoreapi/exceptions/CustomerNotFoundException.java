package ro.myclass.onlineStoreapi.exceptions;

public class CustomerNotFoundException extends RuntimeException{

    public CustomerNotFoundException() {
        super("Customer not found!");
    }
}
