package ro.myclass.onlineStoreapi.exceptions;

public class OrderNotFoundException extends RuntimeException{

    public OrderNotFoundException() {
        super("order not found!");
    }
}
