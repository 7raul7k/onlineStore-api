package ro.myclass.onlineStoreapi.exceptions;

public class StockNotAvailableException extends RuntimeException {

    public StockNotAvailableException() {
        super("Stock not available !");
    }
}
