package ro.myclass.onlineStoreapi.dto;


import lombok.*;
import lombok.experimental.SuperBuilder;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Getter
@Setter
@SuperBuilder
public class ProductCardRequest{


        private int productId;
        private int quantity;
}
