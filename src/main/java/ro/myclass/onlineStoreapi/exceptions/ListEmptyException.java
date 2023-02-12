package ro.myclass.onlineStoreapi.exceptions;

public class ListEmptyException extends RuntimeException{
    public ListEmptyException() {
        super("List is Empty!");
    }
}
