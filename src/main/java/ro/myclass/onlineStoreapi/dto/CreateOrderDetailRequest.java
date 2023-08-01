package ro.myclass.onlineStoreapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import ro.myclass.onlineStoreapi.models.OrderDetail;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class CreateOrderDetailRequest {

    private OrderDetail orderDetail;

    private int customerId;

    private int orderId;


}
