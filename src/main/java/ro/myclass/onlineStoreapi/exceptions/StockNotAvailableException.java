package ro.myclass.onlineStoreapi.exceptions;

public class StockNotAvailableException extends RuntimeException {

    public StockNotAvailableException(String productName) {
        super(productName + " stock is not available !");
    }
}
