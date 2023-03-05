package ro.myclass.onlineStoreapi.dto;



import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
public class CancelOrderRequest {

    private int customerId;

    private int orderId;


}
