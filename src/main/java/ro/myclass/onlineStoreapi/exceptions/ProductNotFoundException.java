package ro.myclass.onlineStoreapi.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ProductNotFoundException extends RuntimeException{

    public ProductNotFoundException() {
        super("Product with  not found!");
    }
}
