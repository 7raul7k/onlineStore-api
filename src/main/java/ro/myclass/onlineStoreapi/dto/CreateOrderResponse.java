package ro.myclass.onlineStoreapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotEmpty;

@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Data
public class CreateOrderResponse  {

    @NotEmpty
    private String message;


    @Override
    public String toString() {
        return message;
    }
}
