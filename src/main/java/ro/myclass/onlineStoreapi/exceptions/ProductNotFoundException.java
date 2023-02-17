package ro.myclass.onlineStoreapi.exceptions;

public class ProductNotFoundException extends RuntimeException{

    public ProductNotFoundException() {
        super("Product not found!");
    }
}
