package ro.myclass.onlineStoreapi.exceptions;

public class CustomerWasFoundException extends RuntimeException{

    public CustomerWasFoundException() {
        super("Customer was found!");
    }
}
