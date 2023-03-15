package ro.myclass.onlineStoreapi.exceptions;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

public class ProductNotFoundException extends RuntimeException{

    public ProductNotFoundException() {
        super("Product with  not found!");
    }
}
