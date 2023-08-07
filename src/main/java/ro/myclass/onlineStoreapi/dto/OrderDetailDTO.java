package ro.myclass.onlineStoreapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class OrderDetailDTO implements Serializable {

    private ProductCardRequest productCardRequest;

    private int orderId;




}
