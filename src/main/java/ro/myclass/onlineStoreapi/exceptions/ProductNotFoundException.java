package ro.myclass.onlineStoreapi.exceptions;

public class ProductNotFoundException extends RuntimeException{

    public ProductNotFoundException(int id) {
        super("Product with id:" + id + " not found!");
    }
}
