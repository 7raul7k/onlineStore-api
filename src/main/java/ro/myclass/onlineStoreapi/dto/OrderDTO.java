package ro.myclass.onlineStoreapi.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class OrderDTO implements Serializable {

    private LocalDate localDate;
    private List<ProductCardRequest> productCardRequests;
    private int id;
    private int customerId;
}
