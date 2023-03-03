package ro.myclass.onlineStoreapi.dto;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotEmpty;

@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class CreateOrderResponse  {

    @NotEmpty
    private String message;


    @Override
    public String toString() {
        return message;
    }
}
