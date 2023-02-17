package ro.myclass.onlineStoreapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTO {

    @NotEmpty
 private String name;
    private double price;
    @NotEmpty
    private String image;
    private int stock;

}
