package ro.myclass.onlineStoreapi.dto;


import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Getter
@Setter
public class ProductCardRequest{


        private int productId;
        private int quantity;
}
