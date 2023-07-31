package ro.myclass.onlineStoreapi.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import ro.myclass.onlineStoreapi.models.OrderDetail;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class OrderDTO {

    private LocalDate localDate;
    private List<OrderDetail> orderDetails;


}
