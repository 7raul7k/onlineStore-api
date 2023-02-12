package ro.myclass.onlineStoreapi.exceptions;

public class CustomerWasFoundExceptions extends RuntimeException{
    public CustomerWasFoundExceptions() {
        super("Customer was found!");
    }
}
