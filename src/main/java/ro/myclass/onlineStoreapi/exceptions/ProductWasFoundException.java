package ro.myclass.onlineStoreapi.exceptions;

public class ProductWasFoundException extends RuntimeException{

    public ProductWasFoundException() {
        super("Product was found");
    }
}
