package ro.myclass.onlineStoreapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import ro.myclass.onlineStoreapi.models.Order;
import ro.myclass.onlineStoreapi.models.Product;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class OrderDetailDTO {

    private double price;
    private int quantity;
    private Order order;
    private Product product;



}
