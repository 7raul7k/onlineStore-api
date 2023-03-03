package ro.myclass.onlineStoreapi.dto;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Getter
@Setter
public class CreateOrderRequest {

    @Min(value = 1)
    private int customerId;

    @NonNull
    private List< @Valid ProductCardRequest> productCardRequests;
}
